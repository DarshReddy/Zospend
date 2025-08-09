package com.assignment.zospend.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.assignment.zospend.ui.list.ExpenseListScreen
import com.assignment.zospend.ui.report.ReportScreen

sealed class Screen(val route: String) {
    object Today : Screen("today")
    object Reports : Screen("reports")
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Today.route,
        modifier = modifier
    ) {
        composable(Screen.Today.route) {
            ExpenseListScreen()
        }
        composable(Screen.Reports.route) {
            ReportScreen()
        }
    }
}
