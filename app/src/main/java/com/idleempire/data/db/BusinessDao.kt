package com.idleempire.data.db

import androidx.room.*
import com.idleempire.data.model.Business
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessDao {

    @Query("SELECT * FROM businesses ORDER BY id ASC")
    fun getAllBusinesses(): Flow<List<Business>>

    @Query("SELECT * FROM businesses ORDER BY id ASC")
    suspend fun getAllBusinessesOnce(): List<Business>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(businesses: List<Business>)

    @Update
    suspend fun update(business: Business)

    @Update
    suspend fun updateAll(businesses: List<Business>)

    @Query("SELECT COUNT(*) FROM businesses")
    suspend fun count(): Int
}
