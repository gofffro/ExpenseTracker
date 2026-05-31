package com.example.expensetracker.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.core.analytics.AnalyticsService
import com.example.expensetracker.core.remoteconfig.RemoteConfigService
import com.example.expensetracker.core.navigation.Screen
import com.example.expensetracker.feature.add.AddExpenseScreen
import com.example.expensetracker.feature.detail.ExpenseDetailScreen
import com.example.expensetracker.feature.list.ExpenseListScreen
import com.example.expensetracker.feature.auth.presentation.LoginScreen
import com.example.expensetracker.feature.about.presentation.AboutScreen

@Composable
fun ExpenseTrackerApp(
    analyticsService: AnalyticsService,
    remoteConfigService: RemoteConfigService
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.ExpenseList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.ExpenseList.route) {
            ExpenseListScreen(
                onAddExpenseClick = { navController.navigate(Screen.AddExpense.route) },
                onExpenseClick = { expense ->
                    navController.navigate(Screen.ExpenseDetail.createRoute(expense.id))
                },
                onAboutClick = { navController.navigate(Screen.About.route) }
            )
        }
        composable(Screen.About.route) {
            AboutScreen(
                analyticsService = analyticsService,
                remoteConfigService = remoteConfigService
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
