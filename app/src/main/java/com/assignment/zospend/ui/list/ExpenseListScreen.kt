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
import androidx.compose.material3.Card
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
import com.assignment.zospend.ui.components.BodyLarge
import com.assignment.zospend.ui.components.BodySmall
import com.assignment.zospend.ui.components.ImageDialog
import com.assignment.zospend.ui.components.TitleLarge
import com.assignment.zospend.ui.components.TitleSmall
import com.assignment.zospend.ui.main.ScreenWrapper
import com.assignment.zospend.ui.theme.ZospendTheme
import java.text.NumberFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ExpenseListScreen(
    viewModel: ExpenseListViewModel = viewModel(),
    onItemClick: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    ScreenWrapper(
        isLoading = uiState.isLoading,
        isEmpty = uiState.expenses.isEmpty(),
        emptyContent = { EmptyState() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TitleLarge(stringResource(id = R.string.all_expenses_title))
            Spacer(Modifier.height(16.dp))
            ExpenseItemsList(
                expenses = uiState.expenses.values.flatten(),
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
private fun ExpenseItemsList(
    expenses: List<Expense>,
    onItemClick: (Long) -> Unit
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
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

    if (selectedImageUri != null) {
        ImageDialog(selectedImageUri.toString(), onDismiss = { selectedImageUri = null })
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
