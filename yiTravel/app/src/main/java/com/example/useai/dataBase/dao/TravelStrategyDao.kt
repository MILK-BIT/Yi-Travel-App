package com.example.useai.dataBase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.useai.dataBase.entity.TravelStrategy
import com.example.useai.helper.MyLatLng

@Dao
interface TravelStrategyDao {
    //增加攻略/写入攻略
    @Insert
    suspend fun add(travelStrategy: TravelStrategy)

    //删除攻略
    @Delete
    suspend fun delete(travelStrategy: TravelStrategy)

    @Update
    suspend fun update(travelStrategy: TravelStrategy)

    @Query("select content from travelstrategy where id = :id")
    suspend fun findByIdReturnContent(id: Long):String

    @Query("select title from travelstrategy where id = :id")
    suspend fun findByIdReturnTitle(id: Long): String


    @Query("SELECT * FROM travelstrategy")
    fun getAllStrategies():LiveData<List<TravelStrategy>> // 直接返回所有数据



}