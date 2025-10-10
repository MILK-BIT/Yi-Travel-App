package com.example.useai.dataBase.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.useai.dataBase.db.MyDataBase
import com.example.useai.dataBase.entity.TravelStrategy
import com.example.useai.dataBase.repository.TravelStrategyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DBViewModel(val app: Application): AndroidViewModel(app) {

    //实例化DataBase对象
    private val db: MyDataBase by lazy {
        Room.databaseBuilder(
            app, MyDataBase::class.java,
            "mydata.db"
        ).build()
    }

    //初始化Repository
    private var travelStrategyRepository: TravelStrategyRepository
    init {
        travelStrategyRepository = TravelStrategyRepository(db)
    }

    fun save(travelStrategy: TravelStrategy){
        viewModelScope.launch (Dispatchers.IO){
            travelStrategyRepository.add(travelStrategy)
        }
    }
    //这里要返回一个List!!!!
    fun getALlStrategies(): LiveData<List<TravelStrategy>>{
         return travelStrategyRepository.getAllStrategies()

    }

    fun changeContent(newContent: String, id: Long){
        viewModelScope.launch (Dispatchers.IO){
            val title: String = travelStrategyRepository.findByIdReturnTitle(id)
            val newTravelStrategy = TravelStrategy(id,title,newContent)
            travelStrategyRepository.update(newTravelStrategy)
        }

    }

    suspend fun getTitleSuspend(id: Long): String {
        return withContext(Dispatchers.IO) {
            travelStrategyRepository.findByIdReturnTitle(id)
        }
    }

    suspend fun getContentSuspend(id: Long): String {
        return withContext(Dispatchers.IO) {
            travelStrategyRepository.findByIdReturnContent(id)
        }
    }

    suspend fun addStrategy(newStrategy: TravelStrategy){
        return travelStrategyRepository.add(newStrategy)
    }
}