package com.example.expensetracker.core.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object ExpenseList : Screen("expense_list")
    object AddExpense : Screen("add_expense")
    object About : Screen("about")
    object ExpenseDetail : Screen("expense_detail/{expenseId}") {
        fun createRoute(expenseId: Int) = "expense_detail/$expenseId"
    }
}
