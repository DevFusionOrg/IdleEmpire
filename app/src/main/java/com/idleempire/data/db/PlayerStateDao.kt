package com.idleempire.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.idleempire.data.model.PlayerState

@Dao
interface PlayerStateDao {

    @Query("SELECT * FROM player_state WHERE id = 1")
    fun getPlayerState(): LiveData<PlayerState>

    @Query("SELECT * FROM player_state WHERE id = 1")
    suspend fun getPlayerStateSync(): PlayerState?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(state: PlayerState)

    @Update
    suspend fun update(state: PlayerState)

    @Query("UPDATE player_state SET coins = :coins, totalCoinsEarned = :total WHERE id = 1")
    suspend fun updateCoins(coins: Double, total: Double)

    @Query("UPDATE player_state SET boostActive = :active, boostEndTime = :endTime WHERE id = 1")
    suspend fun updateBoost(active: Boolean, endTime: Long)

    @Query("UPDATE player_state SET lastSaveTime = :time WHERE id = 1")
    suspend fun updateSaveTime(time: Long)

    @Query("UPDATE player_state SET totalTaps = totalTaps + 1 WHERE id = 1")
    suspend fun incrementTaps()
}
