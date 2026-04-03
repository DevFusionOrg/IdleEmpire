package com.idleempire.data.db

import androidx.room.*
import com.idleempire.data.model.GameState
import kotlinx.coroutines.flow.Flow

@Dao
interface GameStateDao {

    @Query("SELECT * FROM game_state WHERE id = 1")
    fun getGameState(): Flow<GameState>

    @Query("SELECT * FROM game_state WHERE id = 1")
    suspend fun getGameStateOnce(): GameState?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameState: GameState)

    @Update
    suspend fun update(gameState: GameState)
}
