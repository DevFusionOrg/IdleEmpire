package com.idleempire.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.idleempire.data.model.Business
import com.idleempire.data.model.GameState

@Database(
    entities = [Business::class, GameState::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun businessDao(): BusinessDao
    abstract fun gameStateDao(): GameStateDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "idle_empire_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
