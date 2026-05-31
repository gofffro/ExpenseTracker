package com.example.gigaitest.data.api

import com.example.gigaitest.data.repository.GigaChatTokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: GigaChatTokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenManager.getValidToken() }
        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        
        val response = chain.proceed(request)
        
        if (response.code == 401) {
            response.close()
            val newToken = runBlocking { tokenManager.getValidToken() }
            val newRequest = chain.request().newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
            return chain.proceed(newRequest)
        }
        
        return response
    }
}
