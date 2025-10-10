package com.example.useai.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel: ViewModel() {

    //用户名
    private val _name = MutableLiveData<String>("")
    val name: LiveData<String>
        get() = _name
    fun changeName(newName:String){
        _name.postValue(newName)
    }




}