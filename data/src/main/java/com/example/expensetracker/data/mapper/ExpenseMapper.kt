package com.example.expensetracker.data.mapper

import com.example.expensetracker.data.ExpenseEntity
import com.example.expensetracker.domain.model.Expense

fun ExpenseEntity.toDomain(): Expense {
    return Expense(
        id = id,
        title = title,
        amount = amount,
        category = category,
        date = date,
        note = note
    )
}

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        title = title,
        amount = amount,
        category = category,
        date = date,
        note = note
    )
}