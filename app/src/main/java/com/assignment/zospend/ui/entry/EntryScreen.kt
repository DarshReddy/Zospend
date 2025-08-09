package com.assignment.zospend.ui.entry

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.assignment.zospend.R
import com.assignment.zospend.domain.model.Category
import com.assignment.zospend.ui.components.BodyRegular
import com.assignment.zospend.ui.components.LabelMedium
import com.assignment.zospend.ui.components.PrimaryButton
import com.assignment.zospend.ui.components.SecondaryButton
import com.assignment.zospend.ui.components.TitleLarge
import com.assignment.zospend.ui.theme.ZospendTheme
import java.text.NumberFormat

@Composable
fun EntryScreen(
    expenseId: Long = -1,
    onNavigateBack: () -> Unit,
    viewModel: EntryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val duplicateErrorMessage = stringResource(id = R.string.duplicate_expense_error)

    LaunchedEffect(Unit) {
        viewModel.loadExpense(expenseId)
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        viewModel.onReceiptSelected(uri?.toString())
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isSuccess) {
            SuccessAnimation {
                onNavigateBack()
            }
        } else {
            TitleLarge(
                if (uiState.isEditMode) stringResource(id = R.string.edit_expense_title)
                else stringResource(id = R.string.add_expense_title)
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.title,
                onValueChange = viewModel::onTitleChange,
                label = { LabelMedium(stringResource(id = R.string.expense_title_label)) },
                isError = uiState.titleError,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.titleError) {
                BodyRegular(
                    stringResource(id = R.string.expense_title_error),
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.amount,
                onValueChange = viewModel::onAmountChange,
                label = { LabelMedium(stringResource(id = R.string.expense_amount_label)) },
                isError = uiState.amountError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                singleLine = true,
                prefix = { LabelMedium(NumberFormat.getCurrencyInstance(LocalConfiguration.current.locales[0]).currency?.symbol.orEmpty()) },
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.amountError) {
                BodyRegular(
                    stringResource(id = R.string.expense_amount_error),
                    color = MaterialTheme.colorScheme.error,
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
                label = { LabelMedium(stringResource(id = R.string.expense_notes_label)) },
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
                SecondaryButton(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    text = if (uiState.selectedReceiptUri == null) stringResource(id = R.string.add_receipt_button_label)
                    else stringResource(id = R.string.change_receipt_button_label)
                )

                if (uiState.selectedReceiptUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(uiState.selectedReceiptUri)
                                .crossfade(true)
                                .build()
                        ),
                        contentDescription = stringResource(id = R.string.selected_receipt_content_description),
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
                SecondaryButton(
                    onClick = {
                        onNavigateBack()
                    },
                    text = stringResource(id = R.string.cancel_button_label),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .fillMaxWidth(0.5f)
                )
                PrimaryButton(
                    onClick = viewModel::saveExpense,
                    text = if (uiState.isEditMode) stringResource(id = R.string.update_expense_button_label)
                    else stringResource(id = R.string.save_expense_button_label)
                )
            }

            AnimatedVisibility(visible = uiState.isDuplicate) {
                BodyRegular(
                    text = duplicateErrorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun SuccessAnimation(onDismiss: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success_anim))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1, // Set to 1 for a single playback
        isPlaying = true // Start playing immediately
    )
    LaunchedEffect(progress) {
        if (progress == 1.0f) {
            onDismiss()
        }
    }
    LottieAnimation(
        composition = composition,
        iterations = 1,
        modifier = Modifier.fillMaxWidth()
    )
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
            label = { LabelMedium(stringResource(id = R.string.expense_category_label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            Category.entries.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { BodyRegular(selectionOption.name) },
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
        EntryScreen(expenseId = -1, onNavigateBack = {})
    }
}
