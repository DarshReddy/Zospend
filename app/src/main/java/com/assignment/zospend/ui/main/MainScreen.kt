package com.assignment.zospend.ui.main

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.assignment.zospend.R
import com.assignment.zospend.domain.model.Category
import com.assignment.zospend.ui.components.LabelMedium
import com.assignment.zospend.ui.components.TitleRegular
import com.assignment.zospend.ui.entry.EntryScreen
import com.assignment.zospend.ui.navigation.AppNavHost
import com.assignment.zospend.ui.navigation.BottomNavItems
import com.assignment.zospend.ui.report.DailyTotal
import com.assignment.zospend.ui.report.ReportViewModel
import com.assignment.zospend.utils.CsvUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()
    val sheetState = rememberModalBottomSheetState(true)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val reportViewModel: ReportViewModel = viewModel()
    val reportState by reportViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isTodaySelected = navBackStackEntry?.destination?.hasRoute<BottomNavItems.Today>() == true
    val isReportsSelected =
        navBackStackEntry?.destination?.hasRoute<BottomNavItems.Reports>() == true

    Scaffold(
        topBar = {
            TopAppBar(
                title = { TitleRegular(text = stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = onThemeToggle) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Outlined.LightMode else Icons.Outlined.Nightlight,
                            contentDescription = stringResource(id = R.string.toggle_theme_content_description)
                        )
                    }
                    AnimatedVisibility(visible = isReportsSelected) {
                        IconButton(onClick = {
                            shareReport(
                                context,
                                reportState.dailyTotals,
                                reportState.categoryTotals
                            )
                        }) {
                            Icon(
                                Icons.Outlined.Share,
                                contentDescription = stringResource(id = R.string.export_csv_content_description)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            AppBottomBar(
                navController = navController,
                isTodaySelected = isTodaySelected,
                isReportsSelected = isReportsSelected
            )
        },
        floatingActionButton = {
            if (isTodaySelected) {
                FloatingActionButton(onClick = { showBottomSheet = true }) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_expense_content_description)
                    )
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
                    onNavigateBack = {
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
    isTodaySelected: Boolean,
    isReportsSelected: Boolean
) {
    BottomAppBar {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (isTodaySelected) Icons.Filled.Today else Icons.Outlined.Today,
                    contentDescription = stringResource(id = R.string.bottom_nav_today_content_description)
                )
            },
            label = { LabelMedium(stringResource(id = R.string.bottom_nav_today)) },
            selected = isTodaySelected,
            onClick = {
                navController.navigate(BottomNavItems.Today) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (isReportsSelected) Icons.Filled.Analytics else Icons.Outlined.Analytics,
                    contentDescription = stringResource(id = R.string.bottom_nav_reports_content_description)
                )
            },
            label = { LabelMedium(stringResource(id = R.string.bottom_nav_reports)) },
            selected = isReportsSelected,
            onClick = {
                navController.navigate(BottomNavItems.Reports) {
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
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.expense_report_subject))
        putExtra(Intent.EXTRA_TEXT, reportCsv)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.share_report_title)
        )
    )
}
