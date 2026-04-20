package com.example.expensetracker.data.repository

import com.example.expensetracker.data.ExpenseDao
import com.example.expensetracker.data.mapper.toDomain
import com.example.expensetracker.data.mapper.toEntity
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseRepositoryImpl(private val dao: ExpenseDao) : ExpenseRepository {

    override fun getAllExpenses(): Flow<List<Expense>> {
        return dao.getAllExpenses().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTotalAmount(): Flow<Double?> {
        return dao.getTotalAmount()
    }

    override fun getByCategory(category: String): Flow<List<Expense>> {
        return dao.getExpensesByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insert(expense: Expense) {
        dao.insertExpense(expense.toEntity())
    }

    override suspend fun delete(expense: Expense) {
        dao.deleteExpense(expense.toEntity())
    }
}