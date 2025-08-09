package com.assignment.zospend.ui.entry

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.assignment.zospend.domain.model.Category
import com.assignment.zospend.ui.theme.ZospendTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EntryScreen(
    onNavigateToList: () -> Unit,
    onNavigateToReport: () -> Unit,
    viewModel: EntryViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val totalSpentToday by viewModel.totalSpentToday.collectAsState(initial = 0.0)
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        viewModel.onReceiptSelected(uri?.toString())
    }

    LaunchedEffect(uiState.addExpenseResult) {
        uiState.addExpenseResult?.let { result ->
            val message =
                if (result.isSuccess) "Expense added" else result.exceptionOrNull()?.message
                    ?: "An error occurred"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.onAddExpenseResultConsumed()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Total spent today: ${
                NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(totalSpentToday)
            }",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = uiState.title,
            onValueChange = viewModel::onTitleChange,
            label = { Text("Title") },
            isError = uiState.titleError,
            singleLine = true
        )
        if (uiState.titleError) {
            Text("Title cannot be empty", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.amount,
            onValueChange = viewModel::onAmountChange,
            label = { Text("Amount (₹)") },
            isError = uiState.amountError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        if (uiState.amountError) {
            Text(
                "Amount must be greater than 0",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        CategoryDropDown(
            selectedCategory = uiState.category,
            onCategorySelected = viewModel::onCategoryChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.note ?: "",
            onValueChange = { if (it.length <= 100) viewModel.onNoteChange(it) },
            label = { Text("Notes (Optional)") },
            modifier = Modifier.height(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
            Text(if (uiState.receiptUri == null) "Add Receipt" else "Change Receipt")
        }

        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility(visible = uiState.addExpenseResult?.isSuccess == true) {
            InputChip(onClick = {}, label = { Text("✓ Success") }, selected = true)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = viewModel::addExpense) {
            Text("Submit Expense")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onNavigateToList) {
                Text("View List")
            }
            Button(onClick = onNavigateToReport) {
                Text("View Report")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDown(
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable),
            readOnly = true,
            value = selectedCategory.name,
            onValueChange = {},
            label = { Text("Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            Category.entries.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption.name) },
                    onClick = {
                        onCategorySelected(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EntryScreenPreview() {
    ZospendTheme {
        // This preview will not have a real ViewModel, so it won't be fully interactive.
        // For a fully interactive preview, you would need to create a fake ViewModel.
        val fakeViewModel = viewModel { EntryViewModel() }
        EntryScreen(
            viewModel = fakeViewModel,
            onNavigateToList = {},
            onNavigateToReport = {}
        )
    }
}
