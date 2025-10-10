package com.example.useai.helper

import android.content.res.Resources
import com.example.useai.dataBase.entity.TravelStrategy

suspend fun readTxtFileFromRaw(resources: Resources,resourceId: Int): String{
    val allContent =resources.openRawResource(resourceId).use{
        it.bufferedReader(Charsets.UTF_8).use{
            it.readText()
        }
    }
    return allContent
}

fun handleContent(allContent: String,id:Long): TravelStrategy{
    val parts = allContent.split("@@@@@", limit = 2)
    val title = parts[0].trim()
    val content = if (parts.size > 1) parts[1].trim() else "" // "这是内容部分"
    return TravelStrategy(id,title,content)
}

