package com.handofftracker.extra

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.handofftracker.models.RewardModel

@Dao
interface DatabaseDao {
        @Insert
        suspend fun insert(note: RewardModel)

        @Update
        suspend fun update(note: RewardModel)

        @Delete
        suspend fun delete(note: RewardModel)

        @Query("SELECT * FROM rewards")
        fun getAllRewards(): LiveData<List<RewardModel>>

}