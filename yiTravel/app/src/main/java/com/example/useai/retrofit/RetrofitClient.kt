package com.example.useai.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//构建Retrofit相关实例
object RetrofitClient {
    //基础地址
    private const val BASE_URL = "https://api.deepseek.com/"

    //OkHttpClient 配置
    val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)  // 连接超时
        .readTimeout(60, TimeUnit.SECONDS)     // 读取超时
        .writeTimeout(120, TimeUnit.SECONDS)    // 写入超时
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    //Retrofit实例化
    val instance: DeepSeekService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)//使用自定义的 OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DeepSeekService::class.java)//自动生成DeepSeekService接口实现类
    }
}