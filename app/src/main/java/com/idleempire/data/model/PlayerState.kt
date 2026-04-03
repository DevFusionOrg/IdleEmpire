package com.idleempire.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Stores the player's global save state.
 * Only one row ever exists (id = 1).
 */
@Entity(tableName = "player_state")
data class PlayerState(
    @PrimaryKey val id: Int = 1,
    var coins: Double = 0.0,
    var totalCoinsEarned: Double = 0.0,
    var prestigeLevel: Int = 0,
    var prestigeMultiplier: Double = 1.0,   // Increases with each prestige
    var lastSaveTime: Long = System.currentTimeMillis(),
    var tapMultiplier: Double = 1.0,         // Boosted by upgrades
    var boostActive: Boolean = false,
    var boostEndTime: Long = 0L,             // 2x boost from rewarded ads
    var totalTaps: Long = 0L,
    var dailyLoginStreak: Int = 0,
    var lastLoginDate: String = ""           // "yyyy-MM-dd"
)
