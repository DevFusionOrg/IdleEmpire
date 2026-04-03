package com.idleempire.data.repository

import com.idleempire.data.db.BusinessDao
import com.idleempire.data.db.GameStateDao
import com.idleempire.data.model.Business
import com.idleempire.data.model.GameState
import com.idleempire.game.BusinessFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepository(
    private val businessDao: BusinessDao,
    private val gameStateDao: GameStateDao
) {
    val businesses = businessDao.getAllBusinesses()
    val gameState = gameStateDao.getGameState()

    suspend fun initializeIfNeeded() = withContext(Dispatchers.IO) {
        if (businessDao.count() == 0) {
            businessDao.insertAll(BusinessFactory.createDefaultBusinesses())
        }
        if (gameStateDao.getGameStateOnce() == null) {
            gameStateDao.insert(GameState())
        }
    }

    suspend fun updateBusiness(business: Business) = withContext(Dispatchers.IO) {
        businessDao.update(business)
    }

    suspend fun updateAllBusinesses(businesses: List<Business>) = withContext(Dispatchers.IO) {
        businessDao.updateAll(businesses)
    }

    suspend fun updateGameState(state: GameState) = withContext(Dispatchers.IO) {
        gameStateDao.update(state)
    }

    suspend fun getGameStateOnce() = withContext(Dispatchers.IO) {
        gameStateDao.getGameStateOnce()
    }

    suspend fun getAllBusinessesOnce() = withContext(Dispatchers.IO) {
        businessDao.getAllBusinessesOnce()
    }
}
