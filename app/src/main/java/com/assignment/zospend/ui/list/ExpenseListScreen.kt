package com.assignment.zospend.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.assignment.zospend.domain.model.Expense
import com.assignment.zospend.ui.theme.ZospendTheme
import java.text.NumberFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ExpenseListScreen(viewModel: ExpenseListViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SummaryHeader(
            totalAmount = uiState.totalAmount,
            totalCount = uiState.totalCount
        )

        if (uiState.totalCount == 0) {
            EmptyState()
        } else {
            ExpenseItemsList(
                expenses = uiState.expenses.values.flatten()
            )
        }
    }
}

@Composable
private fun SummaryHeader(totalAmount: Long, totalCount: Int) {
    val formattedAmount =
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(totalAmount / 100.0)
    Text(
        text = "Today's Total: $formattedAmount ($totalCount items)",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(16.dp))
}

@Composable
private fun ExpenseItemsList(expenses: List<Expense>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(expenses, key = { it.id }) { expense ->
            ExpenseItem(
                expense = expense,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun ExpenseItem(expense: Expense, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    expense.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                if (!expense.note.isNullOrEmpty()) {
                    Text(expense.note, style = MaterialTheme.typography.bodySmall)
                }
                Text(
                    expense.createdAt.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("hh:mm a")),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Text(
                text = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
                    .format(expense.amount / 100.0),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No expenses today. Tap the + button to add one!")
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseListScreenPreview() {
    ZospendTheme {
        ExpenseListScreen()
    }
}
