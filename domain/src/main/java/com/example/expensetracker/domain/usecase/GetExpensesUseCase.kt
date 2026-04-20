package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetExpensesUseCase(private val repository: ExpenseRepository) {
    operator fun invoke(category: String = "All"): Flow<List<Expense>> {
        return if (category == "All") {
            repository.getAllExpenses()
        } else {
            repository.getByCategory(category)
        }
    }
}