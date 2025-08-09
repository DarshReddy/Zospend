
package com.assignment.zospend.ui.entry

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.assignment.zospend.domain.model.Category
import com.assignment.zospend.ui.theme.ZospendTheme

@Composable
fun EntryScreen(
    onDismiss: () -> Unit,
    viewModel: EntryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        viewModel.onReceiptSelected(uri?.toString())
    }

    LaunchedEffect(uiState.addExpenseResult) {
        uiState.addExpenseResult?.let { result ->
            val message = if (result.isSuccess) {
                "Expense added successfully"
            } else {
                result.exceptionOrNull()?.message ?: "An error occurred"
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.onAddExpenseResultConsumed()
            if (result.isSuccess) {
                onDismiss()
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Expense", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = uiState.title,
            onValueChange = viewModel::onTitleChange,
            label = { Text("Title") },
            isError = uiState.titleError,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (uiState.titleError) {
            Text("Title cannot be empty", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.amount,
            onValueChange = viewModel::onAmountChange,
            label = { Text("Amount (in Rupees)") },
            isError = uiState.amountError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            singleLine = true,
            prefix = { Text("₹") },
            modifier = Modifier.fillMaxWidth()
        )
        if (uiState.amountError) {
            Text(
                "Amount must be a valid number greater than 0",
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
            value = uiState.note,
            onValueChange = viewModel::onNoteChange,
            label = { Text("Notes (Optional, max 100 chars)") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(onClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }) {
                Text(if (uiState.selectedReceiptUri == null) "Add Receipt" else "Change Receipt")
            }

            if (uiState.selectedReceiptUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(uiState.selectedReceiptUri)
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = "Selected receipt",
                    modifier = Modifier.size(64.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = onDismiss, modifier = Modifier.padding(end = 16.dp)) {
                Text("Cancel")
            }
            Button(onClick = viewModel::addExpense) {
                Text("Save Expense")
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
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
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
        EntryScreen(onDismiss = {})
    }
}
