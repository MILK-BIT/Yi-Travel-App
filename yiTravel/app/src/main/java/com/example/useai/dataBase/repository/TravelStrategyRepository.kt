package com.example.useai.dataBase.repository

import androidx.lifecycle.LiveData
import com.example.useai.dataBase.db.MyDataBase
import com.example.useai.dataBase.entity.TravelStrategy

class TravelStrategyRepository(private val db: MyDataBase) {

    suspend fun add(travelStrategy: TravelStrategy){
        return db.travelStrategyDao().add(travelStrategy)
    }

    suspend fun delete(travelStrategy: TravelStrategy){
        return db.travelStrategyDao().delete(travelStrategy)
    }

    suspend fun update(travelStrategy: TravelStrategy){
        return db.travelStrategyDao().update(travelStrategy)
    }

    suspend fun findByIdReturnContent(id: Long):String{
        return db.travelStrategyDao().findByIdReturnContent(id)
    }

    suspend fun findByIdReturnTitle(id: Long):String{
        return db.travelStrategyDao().findByIdReturnTitle(id)
    }

  fun getAllStrategies(): LiveData<List<TravelStrategy>>{
        return db.travelStrategyDao().getAllStrategies()
    }

}