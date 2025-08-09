package com.assignment.zospend.ui.entry

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.assignment.zospend.R
import com.assignment.zospend.domain.model.Category
import com.assignment.zospend.ui.components.BodyRegular
import com.assignment.zospend.ui.components.GenericDropDown
import com.assignment.zospend.ui.components.ImageDialog
import com.assignment.zospend.ui.components.LabelMedium
import com.assignment.zospend.ui.components.PrimaryButton
import com.assignment.zospend.ui.components.SecondaryButton
import com.assignment.zospend.ui.components.StyledOutlinedTextField
import com.assignment.zospend.ui.components.SuccessAnimation
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
    val isFormValid by remember {
        derivedStateOf {
            uiState.amount.isNotBlank() && uiState.title.isNotBlank() && uiState.category != null
        }
    }
    rememberCoroutineScope()
    LocalContext.current
    var clickedImageUri by remember { mutableStateOf<String?>(null) }
    val hapticFeedback = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        viewModel.loadExpense(expenseId)
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        viewModel.onReceiptSelected(uri?.toString())
    }

    if (clickedImageUri?.isNotBlank() == true) {
        ImageDialog(clickedImageUri!!) {
            clickedImageUri = null
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isSuccess) {
            SuccessAnimation {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                onNavigateBack()
            }
        } else {
            TitleLarge(
                if (uiState.isEditMode) stringResource(id = R.string.edit_expense_title)
                else stringResource(id = R.string.add_expense_title)
            )
            Spacer(modifier = Modifier.height(24.dp))
            val titleError = stringResource(id = R.string.expense_title_error)
            StyledOutlinedTextField(
                value = uiState.title,
                onValueChange = viewModel::onTitleChange,
                label = { LabelMedium(stringResource(id = R.string.expense_title_label)) },
                isError = uiState.titleError,
                singleLine = true,
                modifier = Modifier.semantics {
                    if (uiState.titleError) error(titleError)
                }
            )
            if (uiState.titleError) {
                BodyRegular(
                    titleError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            val amountError = stringResource(id = R.string.expense_amount_error)
            StyledOutlinedTextField(
                value = uiState.amount,
                onValueChange = viewModel::onAmountChange,
                label = { LabelMedium(stringResource(id = R.string.expense_amount_label)) },
                isError = uiState.amountError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                singleLine = true,
                prefix = { LabelMedium(NumberFormat.getCurrencyInstance(LocalConfiguration.current.locales[0]).currency?.symbol.orEmpty()) },
                modifier = Modifier.semantics {
                    if (uiState.amountError) error(amountError)
                }
            )
            if (uiState.amountError) {
                BodyRegular(
                    amountError,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            GenericDropDown(
                label = stringResource(id = R.string.expense_category_label),
                items = Category.entries,
                selectedItem = uiState.category,
                onItemSelected = viewModel::onCategoryChange,
                getItemName = { it.name }
            )

            Spacer(modifier = Modifier.height(16.dp))

            StyledOutlinedTextField(
                value = uiState.note,
                onValueChange = viewModel::onNoteChange,
                label = { LabelMedium(stringResource(id = R.string.expense_notes_label)) },
                modifier = Modifier.height(100.dp)
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
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(uiState.selectedReceiptUri)
                            .crossfade(true)
                            .build(),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentDescription = stringResource(id = R.string.selected_receipt_content_description),
                        modifier = Modifier
                            .size(64.dp)
                            .clickable {
                                clickedImageUri = uiState.selectedReceiptUri
                            },
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
                    enabled = isFormValid,
                    text = if (uiState.isEditMode) stringResource(id = R.string.update_expense_button_label)
                    else stringResource(id = R.string.save_expense_button_label)
                )
            }

            val duplicateErrorMessage = stringResource(id = R.string.duplicate_expense_error)
            AnimatedVisibility(visible = uiState.isDuplicate) {
                BodyRegular(
                    text = duplicateErrorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .semantics { error(duplicateErrorMessage) }
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
