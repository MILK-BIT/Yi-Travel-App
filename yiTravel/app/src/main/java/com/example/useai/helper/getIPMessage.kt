package com.example.useai.helper

import android.util.Log
import com.example.useai.BuildConfig
import com.example.useai.retrofit.ChatRequest
import com.example.useai.retrofit.Message
import com.example.useai.retrofit.RetrofitClient
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

suspend fun getIPMessage(strategy: String): String{
    var result = ""
        try {
            // 替换为DeepSeek API Key
            val apiKey = "Bearer ${BuildConfig.DEEPSEEK_API_KEY}"

            val messages = listOf(

                Message(
                    role = "user",
                    content = strategy + "请根据以上攻略给出经度纬度，以如下格式给出，不要有多余字符,不要使用markdown。一定不要有多余字符！！！！"+"{纬度,经度,景点名}#{纬度,经度,景点名}#{纬度,经度,景点名}..."
                )
            )
            coroutineContext.ensureActive()
            val response = RetrofitClient.instance.getChatResponse(
                apiKey = apiKey,
                request = ChatRequest(messages = messages)
            )
            coroutineContext.ensureActive()
            //选第一个
            if (response.choices.isNotEmpty()) {
                result = response.choices[0].message.content
            }
        } catch (e: Exception) {
            result = ""
        }
    Log.d("sendToMap",result)
    return result
}


data class MyLatLng(val latitude:Double, val longitude: Double,val attractionName: String)

fun parseLatLngList(input: String?): List<MyLatLng>? {
    Log.d("parseLatingListString",input?:"空")
    if (input == null)
        return null
    else{
        return input.split("#").map { pair ->
        val cleanPair = pair.removeSurrounding("{", "}")
        val parts = cleanPair.split(",")
        MyLatLng(
            latitude = parts[0].trim().toDoubleOrNull()?:return null,
            longitude = parts[1].trim().toDoubleOrNull()?:return null,
            attractionName = parts[2].trim()
        )
            }
    }
}