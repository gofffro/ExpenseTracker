package com.example.expensetracker.data.repository

import com.example.expensetracker.data.BuildConfig
import com.example.expensetracker.data.remote.GigaChatApi
import com.example.expensetracker.data.remote.GigaChatMessage
import com.example.expensetracker.data.remote.GigaChatRequest
import com.example.expensetracker.domain.repository.AiRepository
import javax.inject.Inject

class AiRepositoryImpl @Inject constructor(
    private val api: GigaChatApi
) : AiRepository {
    private var accessToken: String? = null

    override suspend fun analyzeExpenses(expensesSummary: String): String {
        return try {
            if (accessToken == null) {
                val authHeader = "Basic ${BuildConfig.GIGACHAT_AUTH_KEY}"
                val rqUid = java.util.UUID.randomUUID().toString()
                accessToken = api.getToken(authHeader, rqUid).access_token
            }

            val response = api.getCompletion(
                token = "Bearer $accessToken",
                request = GigaChatRequest(
                    messages = listOf(
                        GigaChatMessage(role = "system", content = "You are a helpful financial assistant."),
                        GigaChatMessage(role = "user", content = expensesSummary)
                    )
                )
            )
            response.choices.firstOrNull()?.message?.content ?: "Анализ недоступен."
        } catch (e: Exception) {
            accessToken = null // Сбрасываем токен при ошибке
            "Ошибка при анализе расходов: ${e.message}"
        }
    }
}
