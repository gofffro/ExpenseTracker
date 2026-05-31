package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.repository.AiRepository
import javax.inject.Inject

class AnalyzeExpensesUseCase @Inject constructor(
    private val aiRepository: AiRepository
) {
    suspend operator fun invoke(expenses: List<Expense>): String {
        if (expenses.isEmpty()) return "Нет расходов для анализа."
        
        val summary = expenses.joinToString("\n") { 
            "${it.date}: ${it.amount} - ${it.category} (${it.title}: ${it.note})"
        }
        
        return aiRepository.analyzeExpenses("Проанализируй эти расходы и дай финансовый совет на русском языке:\n$summary")
    }
}
