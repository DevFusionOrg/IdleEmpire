package com.idleempire.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.idleempire.IdleEmpireApp
import com.idleempire.data.model.Business
import com.idleempire.data.model.GameState
import com.idleempire.data.repository.GameRepository
import com.idleempire.game.GameEngine
import kotlinx.coroutines.*

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val db = (application as IdleEmpireApp).database
    private val repository = GameRepository(db.businessDao(), db.gameStateDao())

    val businesses: LiveData<List<Business>> = repository.businesses.asLiveData()
    val gameState: LiveData<GameState> = repository.gameState.asLiveData()

    private val _offlineEarnings = MutableLiveData<Double>(0.0)
    val offlineEarnings: LiveData<Double> = _offlineEarnings

    private val _tapAnimation = MutableLiveData<Boolean>()
    val tapAnimation: LiveData<Boolean> = _tapAnimation

    private var autoCollectJob: Job? = null

    init {
        viewModelScope.launch {
            repository.initializeIfNeeded()
            calculateOfflineEarnings()
            startAutoCollect()
        }
    }

    fun onTap() {
        viewModelScope.launch {
            val state = repository.getGameStateOnce() ?: return@launch
            val earned = GameEngine.onTap(state)
            repository.updateGameState(state.copy(
                coins = state.coins + earned,
                totalCoinsEarned = state.totalCoinsEarned + earned
            ))
            _tapAnimation.postValue(true)
        }
    }

    fun purchaseBusiness(business: Business) {
        viewModelScope.launch {
            val state = repository.getGameStateOnce() ?: return@launch
            val result = GameEngine.purchaseBusiness(business, state) ?: return@launch
            repository.updateBusiness(result.first)
            repository.updateGameState(result.second)
        }
    }

    fun collectBusiness(business: Business) {
        viewModelScope.launch {
            val state = repository.getGameStateOnce() ?: return@launch
            val result = GameEngine.collectBusiness(business, state)
            repository.updateBusiness(result.first)
            repository.updateGameState(result.second)
        }
    }

    fun buyManager(business: Business) {
        viewModelScope.launch {
            val state = repository.getGameStateOnce() ?: return@launch
            val managerCost = business.currentCost * 10
            if (state.coins < managerCost) return@launch
            repository.updateBusiness(business.copy(hasManager = true))
            repository.updateGameState(state.copy(coins = state.coins - managerCost))
        }
    }

    private suspend fun calculateOfflineEarnings() {
        val state = repository.getGameStateOnce() ?: return
        val businesses = repository.getAllBusinessesOnce()
        val earned = GameEngine.calculateOfflineEarnings(businesses, state)
        if (earned > 0) _offlineEarnings.postValue(earned)
        repository.updateGameState(state.copy(lastOnlineTime = System.currentTimeMillis()))
    }

    fun claimOfflineEarnings(multiplier: Double = 1.0) {
        viewModelScope.launch {
            val state = repository.getGameStateOnce() ?: return@launch
            val earned = (_offlineEarnings.value ?: 0.0) * multiplier
            repository.updateGameState(state.copy(
                coins = state.coins + earned,
                totalCoinsEarned = state.totalCoinsEarned + earned
            ))
            _offlineEarnings.postValue(0.0)
        }
    }

    private fun startAutoCollect() {
        autoCollectJob?.cancel()
        autoCollectJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                val state = repository.getGameStateOnce() ?: continue
                val bizList = repository.getAllBusinessesOnce()
                val result = GameEngine.autoCollect(bizList, state)
                repository.updateAllBusinesses(result.first)
                repository.updateGameState(result.second)
            }
        }
    }

    fun prestige() {
        viewModelScope.launch {
            val state = repository.getGameStateOnce() ?: return@launch
            if (!GameEngine.canPrestige(state)) return@launch
            repository.updateGameState(GameEngine.prestige(state))
            val businesses = repository.getAllBusinessesOnce()
            repository.updateAllBusinesses(businesses.map {
                it.copy(level = 0, hasManager = false, isUnlocked = it.id == 0)
            })
        }
    }

    fun canPrestige(): Boolean = GameEngine.canPrestige(gameState.value ?: GameState())
    fun formatCoins(amount: Double) = GameEngine.formatCoins(amount)

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            repository.getGameStateOnce()?.let {
                repository.updateGameState(it.copy(lastOnlineTime = System.currentTimeMillis()))
            }
        }
    }
}
