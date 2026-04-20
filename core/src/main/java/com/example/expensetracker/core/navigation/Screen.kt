package com.example.expensetracker.core.navigation

sealed class Screen(val route: String) {
    object ExpenseList : Screen("expense_list")
    object AddExpense : Screen("add_expense")
    object ExpenseDetail : Screen("expense_detail/{expenseId}") {
        fun createRoute(expenseId: Int) = "expense_detail/$expenseId"
    }
}
