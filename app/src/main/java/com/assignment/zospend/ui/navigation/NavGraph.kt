package com.assignment.zospend.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.assignment.zospend.ui.entry.EntryScreen
import com.assignment.zospend.ui.list.ExpenseListScreen
import com.assignment.zospend.ui.report.ReportScreen

object AppDestinations {
    const val ENTRY_ROUTE = "entry"
    const val LIST_ROUTE = "list"
    const val REPORT_ROUTE = "report"
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = AppDestinations.ENTRY_ROUTE
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppDestinations.ENTRY_ROUTE) {
            EntryScreen(
                onNavigateToList = { navController.navigate(AppDestinations.LIST_ROUTE) },
                onNavigateToReport = { navController.navigate(AppDestinations.REPORT_ROUTE) }
            )
        }
        composable(AppDestinations.LIST_ROUTE) {
            ExpenseListScreen()
        }
        composable(AppDestinations.REPORT_ROUTE) {
            ReportScreen()
        }
    }
}
