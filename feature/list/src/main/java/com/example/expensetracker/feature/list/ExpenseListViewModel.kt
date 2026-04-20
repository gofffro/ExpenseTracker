package com.example.expensetracker.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.usecase.GetExpensesUseCase
import com.example.expensetracker.domain.usecase.GetTotalAmountUseCase
import com.example.expensetracker.domain.usecase.DeleteExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val getTotalAmountUseCase: GetTotalAmountUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase
) : ViewModel() {

    private val _allExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val allExpenses: StateFlow<List<Expense>> = _allExpenses

    private val _totalAmount = MutableStateFlow<Double>(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount

    init {
        loadExpenses()
        loadTotalAmount()
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
        }
    }

    fun filterByCategory(category: String) {
        viewModelScope.launch {
            getExpensesUseCase(category).collectLatest {
                _allExpenses.value = it
            }
        }
    }
}
