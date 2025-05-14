package com.handofftracker.extra

import android.content.Context
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

abstract class RewardDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: RewardDatabase? = null

        fun getDatabase(context: Context): RewardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    RewardDatabase::class.java,
                    "rewards"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
