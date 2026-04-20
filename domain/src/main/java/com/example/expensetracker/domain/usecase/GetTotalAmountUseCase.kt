package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetTotalAmountUseCase(private val repository: ExpenseRepository) {
    operator fun invoke(): Flow<Double?> {
        return repository.getTotalAmount()
    }
}