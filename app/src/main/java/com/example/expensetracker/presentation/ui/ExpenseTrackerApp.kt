package com.example.expensetracker.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.core.navigation.Screen
import com.example.expensetracker.feature.add.AddExpenseScreen
import com.example.expensetracker.feature.detail.ExpenseDetailScreen
import com.example.expensetracker.feature.list.ExpenseListScreen

@Composable
fun ExpenseTrackerApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.ExpenseList.route) {
        composable(Screen.ExpenseList.route) {
            ExpenseListScreen(
                onAddExpenseClick = { navController.navigate(Screen.AddExpense.route) },
                onExpenseClick = { expense ->
                    navController.navigate(Screen.ExpenseDetail.createRoute(expense.id))
                }
            )
        }
        composable(Screen.AddExpense.route) {
            AddExpenseScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.ExpenseDetail.route) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getString("expenseId")?.toIntOrNull()
            
            if (expenseId != null) {
                ExpenseDetailScreen(
                    expenseId = expenseId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
