package com.assignment.zospend.utils

import com.assignment.zospend.domain.model.Category
import com.assignment.zospend.ui.report.DailyTotal
import java.time.format.DateTimeFormatter

object CsvUtils {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun dailyTotalsToCsv(dailyTotals: List<DailyTotal>): String {
        val header = "Date,Amount (in paise)"
        val rows = dailyTotals.joinToString("\n") {
            "${it.date.format(dateFormatter)},${it.total}"
        }
        return "$header\n$rows"
    }

    fun categoryTotalsToCsv(categoryTotals: Map<Category, Long>): String {
        val header = "Category,Amount (in paise)"
        val rows = categoryTotals.entries.joinToString("\n") { (category, amount) ->
            "\"${category.name}\",$amount"
        }
        return "$header\n$rows"
    }

    fun createReportCsv(
        dailyTotals: List<DailyTotal>,
        categoryTotals: Map<Category, Long>
    ): String {
        val dailyCsv = dailyTotalsToCsv(dailyTotals)
        val categoryCsv = categoryTotalsToCsv(categoryTotals)
        return "Daily Totals (Last 7 Days)\n$dailyCsv\n\nCategory Totals (Last 7 Days)\n$categoryCsv"
    }
}
