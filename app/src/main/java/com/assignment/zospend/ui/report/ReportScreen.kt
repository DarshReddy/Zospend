package com.assignment.zospend.ui.report

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.assignment.zospend.R
import com.assignment.zospend.domain.model.Category
import com.assignment.zospend.ui.components.BodyRegular
import com.assignment.zospend.ui.components.BodySmall
import com.assignment.zospend.ui.components.LabelMedium
import com.assignment.zospend.ui.components.LabelSmall
import com.assignment.zospend.ui.components.TitleRegular
import com.assignment.zospend.ui.main.ScreenWrapper
import com.assignment.zospend.ui.theme.ZospendTheme
import java.text.NumberFormat
import java.time.format.DateTimeFormatter

@Composable
fun ReportScreen(viewModel: ReportViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    ScreenWrapper(
        isLoading = uiState.isLoading,
        isEmpty = uiState.dailyTotals.isEmpty() || uiState.last7DaysTotal == 0L,
        emptyContent = { EmptyState() }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                ReportSummary(uiState.last7DaysTotal)
            }
            item {
                DailyBarChart(uiState.dailyTotals)
            }
            item {
                CategoryTotals(uiState.categoryTotals)
            }
        }
    }
}


@Composable
fun ReportSummary(total: Long) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BodyRegular("Total for last 7 days")
            BodyRegular(
                text = NumberFormat.getCurrencyInstance(LocalConfiguration.current.locales[0])
                    .format(total / 100.0),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun DailyBarChart(dailyTotals: List<DailyTotal>) {
    val maxAmount = dailyTotals.maxOfOrNull { it.total } ?: 1L
    val barColor = MaterialTheme.colorScheme.primary

    Column(modifier = Modifier.fillMaxWidth()) {
        TitleRegular("Daily Totals (Last 7 Days)")
        Spacer(Modifier.height(8.dp))
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val barWidth = size.width / (dailyTotals.size * 2)
            dailyTotals.forEachIndexed { index, dailyTotal ->
                val barHeight = (dailyTotal.total.toFloat() / maxAmount.toFloat()) * size.height
                val startX = (index * 2 + 0.5f) * barWidth
                drawRect(
                    color = barColor,
                    topLeft = Offset(x = startX, y = size.height - barHeight),
                    size = Size(barWidth, barHeight)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val formatter = DateTimeFormatter.ofPattern("d MMM")
            dailyTotals.forEach {
                BodySmall(
                    text = it.date.format(formatter),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
fun CategoryTotals(categoryTotals: Map<Category, Long>) {
    Column {
        LabelMedium("Totals by Category")
        Spacer(Modifier.height(8.dp))
        Card {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                categoryTotals.entries.toList().sortedByDescending { it.value }
                    .forEach { (category, total) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = category.icon,
                                    contentDescription = stringResource(
                                        id = R.string.category_icon_content_description,
                                        category.name
                                    ),
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                LabelSmall(category.name, modifier = Modifier.padding(start = 8.dp))
                            }
                            BodyRegular(
                                NumberFormat.getCurrencyInstance(LocalConfiguration.current.locales[0])
                                    .format(total / 100.0),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        BodyRegular("No expense data available for the last 7 days.", textAlign = TextAlign.Center)
    }
}

@Preview(showBackground = true)
@Composable
fun ReportScreenPreview() {
    ZospendTheme {
        ReportScreen()
    }
}
