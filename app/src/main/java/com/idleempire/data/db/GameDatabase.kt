package com.idleempire.data.db

import android.content.Context
import androidx.room.*
import com.idleempire.data.model.Business
import com.idleempire.data.model.GameState

@Database(entities = [Business::class, GameState::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GameDatabase : RoomDatabase() {

    abstract fun businessDao(): BusinessDao
    abstract fun gameStateDao(): GameStateDao

    companion object {
        @Volatile private var INSTANCE: GameDatabase? = null

        fun getDatabase(context: Context): GameDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "idle_empire_db"
                ).fallbackToDestructiveMigration()
                 .build()
                 .also { INSTANCE = it }
            }
        }
    }
}

class Converters {
    @TypeConverter fun fromBoolean(value: Boolean): Int = if (value) 1 else 0
    @TypeConverter fun toBoolean(value: Int): Boolean = value != 0
}
