package com.idleempire.game

import com.idleempire.data.model.Business
import com.idleempire.data.model.GameState

object GameEngine {

    // Called when player taps the main button
    fun onTap(state: GameState): Double {
        return state.tapPower * state.prestigeMultiplier
    }

    // Buy or upgrade a business
    fun purchaseBusiness(business: Business, state: GameState): Pair<Business, GameState>? {
        val cost = business.currentCost
        if (state.coins < cost) return null

        val updated = business.copy(
            level = business.level + 1,
            isUnlocked = true,
            lastCollectedTime = System.currentTimeMillis()
        )
        val newState = state.copy(coins = state.coins - cost)
        return Pair(updated, newState)
    }

    // Collect income from a single business
    fun collectBusiness(business: Business, state: GameState): Pair<Business, GameState> {
        if (!business.isOwned) return Pair(business, state)

        val now = System.currentTimeMillis()
        val elapsed = now - business.lastCollectedTime
        val cycles = (elapsed / business.incomeInterval).toInt().coerceAtLeast(0)
        val earned = business.currentIncome * cycles * state.prestigeMultiplier

        val updated = business.copy(lastCollectedTime = now)
        val newState = state.copy(
            coins = state.coins + earned,
            totalCoinsEarned = state.totalCoinsEarned + earned
        )
        return Pair(updated, newState)
    }

    // Auto-collect all businesses that have managers
    fun autoCollect(businesses: List<Business>, state: GameState): Pair<List<Business>, GameState> {
        var currentState = state
        val updatedBusinesses = businesses.map { biz ->
            if (biz.hasManager && biz.isOwned) {
                val (updatedBiz, updatedState) = collectBusiness(biz, currentState)
                currentState = updatedState
                updatedBiz
            } else biz
        }
        return Pair(updatedBusinesses, currentState)
    }

    // Calculate how much was earned offline
    fun calculateOfflineEarnings(businesses: List<Business>, state: GameState): Double {
        val now = System.currentTimeMillis()
        val offlineMs = now - state.lastOnlineTime
        val maxOfflineMs = 4 * 60 * 60 * 1000L // 4 hours max

        val effectiveOffline = offlineMs.coerceAtMost(maxOfflineMs)

        return businesses.filter { it.hasManager && it.isOwned }.sumOf { biz ->
            val cycles = (effectiveOffline / biz.incomeInterval).toInt()
            biz.currentIncome * cycles * state.prestigeMultiplier
        }
    }

    // Check if prestige is available (need 1 Trillion total earned)
    fun canPrestige(state: GameState): Boolean {
        return state.totalCoinsEarned >= 1_000_000_000_000.0
    }

    // Perform prestige reset
    fun prestige(state: GameState): GameState {
        return state.copy(
            coins = 0.0,
            totalCoinsEarned = 0.0,
            prestigeLevel = state.prestigeLevel + 1,
            prestigeMultiplier = state.prestigeMultiplier * 2.0,
            tapPower = 1.0,
            tapUpgradeLevel = 0
        )
    }

    fun getUpgradeCost(business: Business): Double {
        return business.currentCost
    }

    fun isMilestone(level: Int): Boolean {
        return BusinessConfig.milestoneLevels.contains(level)
    }

    // Format large numbers nicely
    fun formatCoins(amount: Double): String {
        return when {
            amount >= 1_000_000_000_000_000.0 -> "%.2fQ".format(amount / 1_000_000_000_000_000.0)
            amount >= 1_000_000_000_000.0 -> "%.2fT".format(amount / 1_000_000_000_000.0)
            amount >= 1_000_000_000.0 -> "%.2fB".format(amount / 1_000_000_000.0)
            amount >= 1_000_000.0 -> "%.2fM".format(amount / 1_000_000.0)
            amount >= 1_000.0 -> "%.2fK".format(amount / 1_000.0)
            else -> "%.0f".format(amount)
        }
    }
}
