package com.example.useai.content.HomePage

import android.content.res.Resources
import android.util.Log
import com.example.useai.dataBase.viewModel.DBViewModel
import com.example.useai.helper.readTxtFileFromRaw
import com.example.useai.R  // 确保包名正确
import com.example.useai.helper.handleContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//对图片文件的预处理
suspend fun preprocess(resources: Resources, dbViewModel: DBViewModel) {
    for (i in 1..4) {
        try {
            // 1. 在IO线程读取文件
            val fileContent = withContext(Dispatchers.IO) {
                readTxtFileFromRaw(resources, getPlaceResourceId(i))
            }

            // 2. 处理内容并创建实体
            val strategy = handleContent(fileContent, i.toLong())

            // 3. 插入数据库（确保在IO线程）
            withContext(Dispatchers.IO) {
                dbViewModel.addStrategy(strategy)
            }

        } catch (e: Exception) {
            Log.e("Preprocess", "处理place$i 失败: ${e.message}")
        }
    }
}

fun getPlaceResourceId(index: Int): Int {
    return when(index) {
        1 -> R.raw.place1
        2 -> R.raw.place2
        3 -> R.raw.place3
        4 -> R.raw.place4
        else -> throw IllegalArgumentException("Invalid place index")
    }
}