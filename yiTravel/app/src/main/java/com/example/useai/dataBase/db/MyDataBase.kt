package com.example.useai.dataBase.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.useai.dataBase.dao.TravelStrategyDao
import com.example.useai.dataBase.entity.TravelStrategy

@Database(
    entities = [TravelStrategy::class],
    version = 1,
    exportSchema = false
)
abstract class MyDataBase: RoomDatabase() {
    abstract fun travelStrategyDao(): TravelStrategyDao
}