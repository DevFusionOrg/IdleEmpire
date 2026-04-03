package com.idleempire.data.model

data class Upgrade(
    val id: Int,
    val name: String,
    val description: String,
    val cost: Double,
    val targetBusinessId: Int,  // -1 = global
    val multiplier: Double,
    var isPurchased: Boolean = false
)
