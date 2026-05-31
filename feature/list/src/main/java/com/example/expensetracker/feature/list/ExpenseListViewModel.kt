package com.example.expensetracker.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.analytics.AnalyticsService
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.usecase.GetExpensesUseCase
import com.example.expensetracker.domain.usecase.GetTotalAmountUseCase
import com.example.expensetracker.domain.usecase.DeleteExpenseUseCase
import com.example.expensetracker.domain.usecase.AnalyzeExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val getTotalAmountUseCase: GetTotalAmountUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val analyzeExpensesUseCase: AnalyzeExpensesUseCase,
    private val analyticsService: AnalyticsService
) : ViewModel() {

    private val _allExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val allExpenses: StateFlow<List<Expense>> = _allExpenses

    private val _totalAmount = MutableStateFlow<Double>(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount

    private val _aiAnalysis = MutableStateFlow<String>("Нажмите обновить для анализа...")
    val aiAnalysis: StateFlow<String> = _aiAnalysis.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    init {
        loadExpenses()
        loadTotalAmount()
        trackScreenView()
    }

    private fun trackScreenView() {
        analyticsService.trackEvent("screen_viewed", mapOf("screen_name" to "ExpenseList"))
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            getExpensesUseCase().collectLatest {
                _allExpenses.value = it
            }
        }
    }

    private fun loadTotalAmount() {
        viewModelScope.launch {
            getTotalAmountUseCase().collectLatest {
                _totalAmount.value = it ?: 0.0
            }
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            deleteExpenseUseCase(expense)
            analyticsService.trackEvent("expense_deleted", mapOf("category" to expense.category))
        }
    }

    fun filterByCategory(category: String) {
        analyticsService.trackEvent("category_filtered", mapOf("category" to category))
        viewModelScope.launch {
            getExpensesUseCase(category).collectLatest {
                _allExpenses.value = it
            }
        }
    }

    fun refreshAiAnalysis() {
        viewModelScope.launch {
            _isAiLoading.value = true
            _aiAnalysis.value = analyzeExpensesUseCase(_allExpenses.value)
            _isAiLoading.value = false
        }
    }
}
