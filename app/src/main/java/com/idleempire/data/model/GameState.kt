package com.idleempire.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_state")
data class GameState(
    @PrimaryKey val id: Int = 1,
    var coins: Double = 0.0,
    var totalCoinsEarned: Double = 0.0,
    var prestigeLevel: Int = 0,
    var prestigeMultiplier: Double = 1.0,
    var lastOnlineTime: Long = System.currentTimeMillis(),
    var tapPower: Double = 1.0,
    var tapUpgradeLevel: Int = 0,
    var boostActive: Boolean = false,
    var boostEndTime: Long = 0L
)
