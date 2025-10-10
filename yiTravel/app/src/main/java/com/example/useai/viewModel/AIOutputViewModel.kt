package com.example.useai.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AIOutputViewModel: ViewModel() {
    private val _AIOutput = MutableLiveData<String>()
    val AIOutput:LiveData<String>
        get() = _AIOutput

    fun changeAIOutput(newOutput: String){
        _AIOutput.postValue(newOutput)
    }

    private val _AIOutputSmall = MutableLiveData<String>()
    val AIOutputSmall:LiveData<String>
        get() = _AIOutputSmall

    fun changeAIOutputSmall(newOutput: String){
        _AIOutputSmall.postValue(newOutput)
    }
}