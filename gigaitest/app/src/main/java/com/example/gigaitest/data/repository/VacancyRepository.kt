package com.example.gigaitest.data.repository

import com.example.gigaitest.data.api.GigaChatApi
import com.example.gigaitest.data.model.ChatCompletionRequest
import com.example.gigaitest.data.model.ChatMessage
import javax.inject.Inject
import javax.inject.Singleton

interface VacancyRepository {
    suspend fun analyzeVacancy(vacancyText: String, skillsText: String): Result<String>
}

@Singleton
class VacancyRepositoryImpl @Inject constructor(
    private val gigaChatApi: GigaChatApi
) : VacancyRepository {

    override suspend fun analyzeVacancy(vacancyText: String, skillsText: String): Result<String> {
        return try {
            val systemPrompt = """
                Ты - опытный IT-рекрутер и карьерный консультант. Пользователь предоставил два текста:
                1. Текст вакансии: {vacancyText}
                2. Мои навыки: {skillsText}

                Твоя задача -- дать три блока рекомендаций на русском языке, дружелюбным тоном.
                Первый блок: «Оценка соответствия» -- кратко напиши, насколько навыки пользователя соответствуют требованиям вакансии, выдели сильные стороны и заметные пробелы.
                Второй блок: «Что подтянуть» -- перечисли 2-3 конкретные темы или технологии, которые стоит изучить перед откликом.
                Третий блок: «Вопросы работодателю» -- предложи 2-3 уточняющих вопроса, которые можно задать на собеседовании, чтобы показать заинтересованность.
                Не добавляй лишних вступлений и заключений, сразу переходи к блокам.
            """.trimIndent()
                .replace("{vacancyText}", vacancyText)
                .replace("{skillsText}", skillsText)

            val request = ChatCompletionRequest(
                messages = listOf(
                    ChatMessage(role = "system", content = systemPrompt)
                )
            )

            val response = gigaChatApi.createChatCompletion(request)
            val resultText = response.choices.firstOrNull()?.message?.content
            if (resultText != null) {
                Result.success(resultText)
            } else {
                Result.failure(Exception("Empty response from GigaChat"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
