package com.example.expensetracker.domain.repository

import com.example.expensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    fun getTotalAmount(): Flow<Double?>
    fun getByCategory(category: String): Flow<List<Expense>>
    suspend fun insert(expense: Expense)
    suspend fun delete(expense: Expense)
}