package com.assignment.zospend.ui.report

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.zospend.data.ServiceLocator
import com.assignment.zospend.data.local.Expense
import com.assignment.zospend.data.mock.MockExpense
import com.assignment.zospend.domain.model.Category
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.random.Random

data class ReportUiState(
    val dailyTotals: List<DailyTotal> = emptyList(),
    val categoryTotals: Map<Category, Long> = emptyMap(),
    val last7DaysTotal: Long = 0L,
    val isLoading: Boolean = false
)

data class DailyTotal(
    val date: LocalDate,
    val total: Long
)

class ReportViewModel(application: Application) : AndroidViewModel(application) {

    private val expenseRepository = ServiceLocator.provideRepository(application)

    private fun generateMockExpenses(): List<Expense> {
        val mockExpenses = mutableListOf<Expense>()
        val today = LocalDate.now()
        for (i in 1..6) {
            val date = today.minusDays(i.toLong())
            repeat((0..Random.nextInt(1, 4)).count()) {
                val instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant()
                    .plus(
                        (0..86400).random().toLong(),
                        ChronoUnit.SECONDS
                    ) // Random time within the day
                mockExpenses.add(
                    MockExpense.next(createdAt = instant)
                )
            }
        }
        return mockExpenses
    }

    val uiState: StateFlow<ReportUiState> = expenseRepository.allExpenses()
        .map { expenses ->
            val mockExpenses = generateMockExpenses()
            val combinedExpenses = expenses + mockExpenses

            val sevenDaysAgo = Instant.now().minus(7, ChronoUnit.DAYS)
            val recentExpenses = combinedExpenses.filter { it.createdAt.isAfter(sevenDaysAgo) }

            // Calculate daily totals for the last 7 days
            val today = LocalDate.now()
            val last7Days = (0..6).map { today.minusDays(it.toLong()) }
            val dailyTotalsMap = recentExpenses
                .groupBy { it.createdAt.atZone(ZoneId.systemDefault()).toLocalDate() }
                .mapValues { (_, exps) -> exps.sumOf { it.amount } }

            val dailyTotals = last7Days.map { date ->
                DailyTotal(date, dailyTotalsMap[date] ?: 0L)
            }.reversed() // Reverse to have the oldest day first for charting

            // Calculate category totals for the last 7 days
            val categoryTotals = recentExpenses
                .groupBy { it.category }
                .mapValues { (_, exps) -> exps.sumOf { it.amount } }

            val last7DaysTotal = recentExpenses.sumOf { it.amount }

            ReportUiState(
                dailyTotals = dailyTotals,
                categoryTotals = categoryTotals,
                last7DaysTotal = last7DaysTotal,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ReportUiState(isLoading = true)
        )
}
