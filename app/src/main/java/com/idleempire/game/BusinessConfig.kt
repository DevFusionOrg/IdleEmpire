package com.idleempire.game

import com.idleempire.data.model.Business

/**
 * Central config for all businesses in the game.
 */
object BusinessConfig {

    fun defaultBusinesses(): List<Business> = listOf(
        Business(
            id = 0,
            name = "Lemonade Stand",
            emoji = "🍋",
            baseCost = 5.0,
            baseIncome = 1.0,
            incomeInterval = 1_000,      // 1 second
            managerCost = 50.0,
            isUnlocked = true,              // First one is free/auto-unlocked
            level = 1
        ),
        Business(
            id = 1,
            name = "Pizza Shop",
            emoji = "🍕",
            baseCost = 75.0,
            baseIncome = 8.0,
            incomeInterval = 3_000,      // 3 seconds
            managerCost = 500.0
        ),
        Business(
            id = 2,
            name = "Car Wash",
            emoji = "🚗",
            baseCost = 720.0,
            baseIncome = 47.0,
            incomeInterval = 6_000,      // 6 seconds
            managerCost = 3_600.0
        ),
        Business(
            id = 3,
            name = "Donut Factory",
            emoji = "🍩",
            baseCost = 8_640.0,
            baseIncome = 400.0,
            incomeInterval = 12_000,     // 12 seconds
            managerCost = 43_200.0
        ),
        Business(
            id = 4,
            name = "Shrimp Boat",
            emoji = "🦐",
            baseCost = 103_680.0,
            baseIncome = 2_900.0,
            incomeInterval = 30_000,     // 30 seconds
            managerCost = 518_400.0
        ),
        Business(
            id = 5,
            name = "Hockey Team",
            emoji = "🏒",
            baseCost = 1_244_160.0,
            baseIncome = 21_600.0,
            incomeInterval = 60_000,     // 1 minute
            managerCost = 6_220_800.0
        ),
        Business(
            id = 6,
            name = "Movie Studio",
            emoji = "🎬",
            baseCost = 14_929_920.0,
            baseIncome = 172_800.0,
            incomeInterval = 180_000,    // 3 minutes
            managerCost = 74_649_600.0
        ),
        Business(
            id = 7,
            name = "Bank",
            emoji = "🏦",
            baseCost = 179_159_040.0,
            baseIncome = 1_728_000.0,
            incomeInterval = 360_000,    // 6 minutes
            managerCost = 895_795_200.0
        )
    )

    // Milestone levels where special events/ads are triggered
    val milestoneLevels = setOf(10, 25, 50, 100, 200, 300, 400)

    // How many upgrades before showing an interstitial ad
    const val UPGRADES_BEFORE_INTERSTITIAL = 3

    // Offline earnings cap: max 8 hours worth
    const val MAX_OFFLINE_HOURS = 8L
}
