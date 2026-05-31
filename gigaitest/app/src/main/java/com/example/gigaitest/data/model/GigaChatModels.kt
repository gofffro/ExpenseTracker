package com.example.gigaitest.data.model

import com.google.gson.annotations.SerializedName

data class GigaTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_at") val expiresAt: Long
)

data class GigaTokenEntity(
    val accessToken: String,
    val expiresAt: Long
)

data class ChatCompletionRequest(
    val model: String = "GigaChat",
    val messages: List<ChatMessage>,
    val temperature: Double = 0.5,
    @SerializedName("max_tokens") val maxTokens: Int = 1000,
    val stream: Boolean = false
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatCompletionResponse(
    val choices: List<ChatChoice>
)

data class ChatChoice(
    val message: ChatMessage,
    @SerializedName("finish_reason") val finishReason: String
)
