package com.example.useai.dataBase.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.useai.helper.MyLatLng

@Entity(tableName = "travelstrategy")
data class TravelStrategy(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var title: String,
    var content: String
)