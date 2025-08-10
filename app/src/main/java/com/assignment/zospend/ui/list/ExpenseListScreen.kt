package com.assignment.zospend.ui.list

import android.net.Uri
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SelectableDates
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.assignment.zospend.R
import com.assignment.zospend.data.local.Expense
import com.assignment.zospend.domain.model.Category
import com.assignment.zospend.ui.components.BodyLarge
import com.assignment.zospend.ui.components.BodySmall
import com.assignment.zospend.ui.components.ImageDialog
import com.assignment.zospend.ui.components.TitleLarge
import com.assignment.zospend.ui.components.TitleSmall
import com.assignment.zospend.ui.main.ScreenWrapper
import com.assignment.zospend.ui.theme.ZospendTheme
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun ExpenseListScreen(
    viewModel: ExpenseListViewModel = viewModel(),
    onItemClick: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        ExpenseDatePickerDialog(
            onDismiss = { showDatePicker = false },
            onDateSelected = {
                viewModel.onDateSelected(it)
                showDatePicker = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DateNavigationBar(
            selectedDate = uiState.selectedDate,
            onPreviousClick = viewModel::onPreviousDayClicked,
            onNextClick = viewModel::onNextDayClicked,
            onDateIconClick = { showDatePicker = true }
        )
        Spacer(Modifier.height(16.dp))

        TotalsHeader(
            totalAmount = uiState.totalAmount,
            totalCount = uiState.totalCount
        )

        Spacer(Modifier.height(16.dp))

        ScreenWrapper(
            isLoading = uiState.isLoading,
            isEmpty = uiState.expenses.isEmpty(),
            emptyContent = { EmptyState() }
        ) {
            ExpenseItemsList(
                expenses = uiState.expenses,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
private fun TotalsHeader(totalAmount: Long, totalCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TitleLarge(text = "Total Expenses: $totalCount")
        TitleLarge(
            text = NumberFormat.getCurrencyInstance(LocalConfiguration.current.locales[0])
                .format(totalAmount / 100.0)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpenseDatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= Instant.now().toEpochMilli()
            }
        }
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
                }
            ) {
                BodyLarge(stringResource(id = android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                BodyLarge(stringResource(id = android.R.string.cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}


@Composable
fun DateNavigationBar(
    selectedDate: LocalDate,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onDateIconClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousClick) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.previous_day)
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleLarge(
                if (selectedDate != LocalDate.now()) {
                    selectedDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                } else {
                    stringResource(id = R.string.today_expenses_title)
                },
                textAlign = TextAlign.Center
            )
            IconButton(onClick = onDateIconClick) {
                Icon(
                    Icons.Default.CalendarMonth,
                    contentDescription = stringResource(id = R.string.select_date)
                )
            }
        }

        IconButton(onClick = onNextClick, enabled = selectedDate != LocalDate.now()) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(id = R.string.next_day)
            )
        }
    }
}

@Composable
private fun ExpenseItemsList(
    expenses: Map<Category?, List<Expense>>,
    onItemClick: (Long) -> Unit
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        expenses.forEach { (category, expenses) ->
            item {
                CategoryHeader(
                    category = category,
                    totalAmount = expenses.sumOf { it.amount }
                )
            }
            items(expenses, key = { it.id }) { expense ->
                ExpenseItem(
                    expense = expense,
                    onReceiptClick = { uri ->
                        selectedImageUri = uri
                    },
                    onItemClick = { onItemClick(expense.id) },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }

    if (selectedImageUri != null) {
        ImageDialog(selectedImageUri.toString(), onDismiss = { selectedImageUri = null })
    }
}

@Composable
private fun CategoryHeader(category: Category?, totalAmount: Long) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TitleSmall(text = category?.name ?: "Uncategorized")
        TitleSmall(
            text = NumberFormat.getCurrencyInstance(LocalConfiguration.current.locales[0])
                .format(totalAmount / 100.0)
        )
    }
}

@Composable
private fun ExpenseItem(
    expense: Expense,
    onReceiptClick: (Uri) -> Unit,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.clickable(onClick = onItemClick)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                TitleSmall(
                    expense.title
                )
                if (!expense.note.isNullOrEmpty()) {
                    BodySmall(expense.note)
                }
                BodySmall(
                    expense.createdAt.atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("hh:mm a"))
                )
            }
            expense.receiptUri?.let { uriString ->
                val uri = uriString.toUri()
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(uri)
                        .crossfade(true)
                        .build(),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    contentDescription = stringResource(id = R.string.selected_receipt_content_description),
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 12.dp)
                        .clickable { onReceiptClick(uri) }
                )
            }
            TitleSmall(
                text = NumberFormat.getCurrencyInstance(LocalConfiguration.current.locales[0])
                    .format(expense.amount / 100.0)
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        BodyLarge(stringResource(id = R.string.no_expenses_yet), textAlign = TextAlign.Center)
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseListScreenPreview() {
    ZospendTheme {
        ExpenseListScreen()
    }
}
