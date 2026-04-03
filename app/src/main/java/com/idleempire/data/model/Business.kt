package com.idleempire.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "businesses")
data class Business(
    @PrimaryKey val id: Int,
    val name: String,
    val emoji: String,
    val baseCost: Double,
    val baseIncome: Double,
    val incomeInterval: Long,   // ms between earnings
    val managerCost: Double,
    var level: Int = 0,
    var isUnlocked: Boolean = false,
    var hasManager: Boolean = false,
    var lastCollectedTime: Long = 0L
) {
    val currentCost: Double
        get() = baseCost * Math.pow(1.15, level.toDouble())

    val currentIncome: Double
        get() = baseIncome * level * (if (hasManager) 1.5 else 1.0)

    val isOwned: Boolean
        get() = level > 0
        
    fun unlockCost() = baseCost
    fun incomePerCycle() = currentIncome
}
