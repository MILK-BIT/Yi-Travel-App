package com.example.useai.retrofit

// DeepSeekService.kt
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


//API接口/Service接口
interface DeepSeekService {
    @POST("v1/chat/completions")
    suspend fun getChatResponse(
        @Header("Authorization") apiKey: String,//认证头
        @Body request: ChatRequest//请求体
    ): ChatResponse//相应体
}

//请求体
data class ChatRequest(
    val model: String = "deepseek-chat",
    val messages: List<Message>,//对话历史记录
    val max_tokens: Int = 2000
)

//请求体中的对话消息
data class Message(
    val role: String,
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>//choices列表，有列表所对应的回复可能
)


//Response中可能回复的信息
data class Choice(
    val message: Message
)