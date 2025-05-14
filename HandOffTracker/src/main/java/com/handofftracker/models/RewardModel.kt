package com.handofftracker.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rewards")
data class RewardModel(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val storeNumber: String,
    val eventUserid: String,
    val dateTime: String,
    val point: Int
)
