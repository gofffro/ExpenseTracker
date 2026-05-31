package com.example.gigaitest.data.api

import com.example.gigaitest.data.model.ChatCompletionRequest
import com.example.gigaitest.data.model.ChatCompletionResponse
import com.example.gigaitest.data.model.GigaTokenResponse
import retrofit2.http.*

interface GigaOAuthApi {
    @FormUrlEncoded
    @POST("api/v2/oauth")
    suspend fun getAccessToken(
        @Header("RqUID") rqUid: String,
        @Header("Authorization") authorizationKey: String,
        @Field("scope") scope: String = "GIGACHAT_API_PERS"
    ): GigaTokenResponse
}

interface GigaChatApi {
    @POST("api/v1/chat/completions")
    suspend fun createChatCompletion(
        @Body request: ChatCompletionRequest
    ): ChatCompletionResponse
}
