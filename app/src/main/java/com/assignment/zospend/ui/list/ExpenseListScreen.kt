package com.assignment.zospend.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.assignment.zospend.domain.model.Expense
import com.assignment.zospend.ui.theme.ZospendTheme
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ExpenseListScreen(viewModel: ExpenseListViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        ControlsHeader(
            selectedDate = uiState.selectedDate,
            isGrouped = uiState.isGroupedByCategory,
            onDateClicked = { showDatePicker = true },
            onGroupingToggled = viewModel::onGroupingToggled
        )

        SummaryHeader(
            totalAmount = uiState.totalAmount,
            totalCount = uiState.totalCount
        )

        if (uiState.totalCount == 0) {
            EmptyState()
        } else {
            ExpenseItemsList(
                expensesMap = uiState.expenses,
                isGrouped = uiState.isGroupedByCategory
            )
        }
    }

    if (showDatePicker) {
        ExpenseDatePickerDialog(
            initialDate = uiState.selectedDate,
            onDateSelected = {
                viewModel.onDateSelected(it)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
private fun ControlsHeader(
    selectedDate: LocalDate,
    isGrouped: Boolean,
    onDateClicked: () -> Unit,
    onGroupingToggled: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
            modifier = Modifier.clickable(onClick = onDateClicked),
            style = MaterialTheme.typography.titleMedium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Group by Category", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.width(8.dp))
            Switch(checked = isGrouped, onCheckedChange = onGroupingToggled)
        }
    }
}

@Composable
private fun SummaryHeader(totalAmount: Long, totalCount: Int) {
    val formattedAmount =
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(totalAmount / 100.0)
    Text(
        text = "Total: $formattedAmount ($totalCount items)",
        modifier = Modifier.padding(horizontal = 16.dp),
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(8.dp))
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ExpenseItemsList(expensesMap: Map<out Any, List<Expense>>, isGrouped: Boolean) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        expensesMap.forEach { (header, expenses) ->
            if (isGrouped) {
                stickyHeader {
                    CategoryHeader(name = header.toString())
                }
            }
            items(expenses, key = { it.id }) { expense ->
                ExpenseItem(
                    expense = expense,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun CategoryHeader(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun ExpenseItem(expense: Expense, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
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
        Text("No expenses for this day. Add one from the Entry screen!")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpenseDatePickerDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
            .toEpochMilli()
    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(
                            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        )
                    }
                },
                enabled = datePickerState.selectedDateMillis != null
            ) { Text("OK") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseListScreenPreview() {
    ZospendTheme {
        ExpenseListScreen(viewModel())
    }
}
