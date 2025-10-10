package com.example.useai.dataBase.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DBViewModelFactory(val app: Application): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>):T{
        if(modelClass.isAssignableFrom(DBViewModel::class.java)){
            return DBViewModel(app) as T
        }
        throw IllegalArgumentException("UnKnown viewModel Class")
    }
}