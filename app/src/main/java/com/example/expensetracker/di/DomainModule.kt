package com.example.expensetracker.di

import com.example.expensetracker.domain.repository.ExpenseRepository
import com.example.expensetracker.domain.usecase.AddExpenseUseCase
import com.example.expensetracker.domain.usecase.DeleteExpenseUseCase
import com.example.expensetracker.domain.usecase.GetExpensesUseCase
import com.example.expensetracker.domain.usecase.GetTotalAmountUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideGetExpensesUseCase(repository: ExpenseRepository): GetExpensesUseCase {
        return GetExpensesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddExpenseUseCase(repository: ExpenseRepository): AddExpenseUseCase {
        return AddExpenseUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteExpenseUseCase(repository: ExpenseRepository): DeleteExpenseUseCase {
        return DeleteExpenseUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetTotalAmountUseCase(repository: ExpenseRepository): GetTotalAmountUseCase {
        return GetTotalAmountUseCase(repository)
    }
}
