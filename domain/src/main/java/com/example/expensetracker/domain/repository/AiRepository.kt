package com.example.expensetracker.domain.repository

interface AiRepository {
    suspend fun analyzeExpenses(expensesSummary: String): String
}
