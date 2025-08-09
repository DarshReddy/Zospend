package com.assignment.zospend.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.assignment.zospend.ui.entry.EntryScreen
import com.assignment.zospend.ui.list.ExpenseListScreen
import com.assignment.zospend.ui.report.ReportScreen
import kotlinx.serialization.Serializable

sealed interface BottomNavItems {
    @Serializable
    data object Today : BottomNavItems

    @Serializable
    data object Reports : BottomNavItems
}

@Serializable
sealed interface Screen {
    @Serializable
    data object BottomNav : Screen

    @Serializable
    data class Entry(val expenseId: Long = -1) : Screen
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.BottomNav,
        modifier = modifier
    ) {
        navigation<Screen.BottomNav>(startDestination = BottomNavItems.Today) {
            composable<BottomNavItems.Today> {
                ExpenseListScreen(
                    onItemClick = {
                        navController.navigate(Screen.Entry(it))
                    }
                )
            }
            composable<BottomNavItems.Reports> {
                ReportScreen()
            }
        }
        composable<Screen.Entry> {
            val args = it.toRoute<Screen.Entry>()
            EntryScreen(
                expenseId = args.expenseId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
