package com.example.expensetracker.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.*

interface GigaChatApi {
    @FormUrlEncoded
    @POST("https://ngw.devices.sberbank.ru:9443/api/v2/oauth")
    suspend fun getToken(
        @Header("Authorization") authHeader: String,
        @Header("RqUID") rqUid: String,
        @Field("scope") scope: String = "GIGACHAT_API_PERS"
    ): GigaChatTokenResponse

    @POST("api/v1/chat/completions")
    suspend fun getCompletion(
        @Header("Authorization") token: String,
        @Body request: GigaChatRequest
    ): GigaChatResponse
}

data class GigaChatTokenResponse(
    @SerializedName("access_token") val access_token: String,
    @SerializedName("expires_at") val expires_at: Long
)

data class GigaChatRequest(
    val model: String = "GigaChat",
    val messages: List<GigaChatMessage>,
    val stream: Boolean = false
)

data class GigaChatMessage(
    val role: String,
    val content: String
)

data class GigaChatResponse(
    val choices: List<GigaChatChoice>
)

data class GigaChatChoice(
    val message: GigaChatMessage
)
