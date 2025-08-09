package com.assignment.zospend.ui.main

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.assignment.zospend.domain.model.Category
import com.assignment.zospend.ui.entry.EntryScreen
import com.assignment.zospend.ui.navigation.AppNavHost
import com.assignment.zospend.ui.navigation.Screen
import com.assignment.zospend.ui.report.DailyTotal
import com.assignment.zospend.ui.report.ReportViewModel
import com.assignment.zospend.utils.CsvUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val sheetState = rememberModalBottomSheetState(true)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val reportViewModel = viewModel { ReportViewModel() }
    val reportState by reportViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Zospend") },
                actions = {
                    AnimatedVisibility(visible = currentRoute == Screen.Reports.route) {
                        IconButton(onClick = {
                            shareReport(
                                context,
                                reportState.dailyTotals,
                                reportState.categoryTotals
                            )
                        }) {
                            Icon(Icons.Default.Share, contentDescription = "Export CSV")
                        }
                    }
                }
            )
        },
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = currentRoute
            )
        },
        floatingActionButton = {
            if (currentRoute == Screen.Today.route) {
                FloatingActionButton(onClick = { showBottomSheet = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Expense")
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavHost(navController = navController)
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                EntryScreen(
                    onDismiss = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AppBottomBar(
    navController: NavController,
    currentRoute: String?
) {
    BottomAppBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Today") },
            label = { Text("Today") },
            selected = currentRoute == Screen.Today.route,
            onClick = {
                navController.navigate(Screen.Today.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Default.List, contentDescription = "Reports") },
            label = { Text("Reports") },
            selected = currentRoute == Screen.Reports.route,
            onClick = {
                navController.navigate(Screen.Reports.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
    }
}

private fun shareReport(
    context: Context,
    dailyTotals: List<DailyTotal>,
    categoryTotals: Map<Category, Long>
) {
    val reportCsv = CsvUtils.createReportCsv(dailyTotals, categoryTotals)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/csv"
        putExtra(Intent.EXTRA_SUBJECT, "Zospend Expense Report")
        putExtra(Intent.EXTRA_TEXT, reportCsv)
    }
    context.startActivity(Intent.createChooser(intent, "Share Report"))
}
