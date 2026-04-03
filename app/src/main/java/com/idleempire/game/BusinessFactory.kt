package com.idleempire.game

import com.idleempire.data.model.Business

object BusinessFactory {

    fun createDefaultBusinesses(): List<Business> = listOf(
        Business(
            id = 0, name = "Lemonade Stand", emoji = "🍋",
            baseCost = 10.0, baseIncome = 1.0,
            incomeInterval = 1000L, managerCost = 50.0, isUnlocked = true
        ),
        Business(
            id = 1, name = "Pizza Shop", emoji = "🍕",
            baseCost = 150.0, baseIncome = 10.0,
            incomeInterval = 3000L, managerCost = 500.0
        ),
        Business(
            id = 2, name = "Car Wash", emoji = "🚗",
            baseCost = 1_500.0, baseIncome = 80.0,
            incomeInterval = 6000L, managerCost = 3600.0
        ),
        Business(
            id = 3, name = "Bakery", emoji = "🥐",
            baseCost = 12_000.0, baseIncome = 500.0,
            incomeInterval = 12000L, managerCost = 43200.0
        ),
        Business(
            id = 4, name = "Cinema", emoji = "🎬",
            baseCost = 100_000.0, baseIncome = 3_000.0,
            incomeInterval = 24000L, managerCost = 518400.0
        ),
        Business(
            id = 5, name = "Hotel", emoji = "🏨",
            baseCost = 1_000_000.0, baseIncome = 20_000.0,
            incomeInterval = 48000L, managerCost = 6220800.0
        ),
        Business(
            id = 6, name = "Oil Company", emoji = "⛽",
            baseCost = 10_000_000.0, baseIncome = 150_000.0,
            incomeInterval = 96000L, managerCost = 74649600.0
        ),
        Business(
            id = 7, name = "Tech Empire", emoji = "💻",
            baseCost = 100_000_000.0, baseIncome = 1_000_000.0,
            incomeInterval = 180000L, managerCost = 895795200.0
        )
    )
}
