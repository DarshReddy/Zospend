package com.assignment.zospend.ui.entry

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.zospend.data.ServiceLocator
import com.assignment.zospend.data.local.Expense
import com.assignment.zospend.domain.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant

data class EntryUiState(
    val title: String = "",
    val amount: String = "",
    val category: Category = Category.FOOD,
    val note: String = "",
    val selectedReceiptUri: String? = null,
    val titleError: Boolean = false,
    val amountError: Boolean = false,
    val isDuplicate: Boolean = false,
    val isSuccess: Boolean = false
)

class EntryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ServiceLocator.provideRepository(application)
    private val _uiState = MutableStateFlow(EntryUiState())
    val uiState = _uiState.asStateFlow()

    fun onTitleChange(newTitle: String) {
        _uiState.value =
            _uiState.value.copy(title = newTitle, titleError = false, isDuplicate = false)
    }

    fun onAmountChange(newAmount: String) {
        if (newAmount.all { it.isDigit() }) {
            _uiState.value =
                _uiState.value.copy(amount = newAmount, amountError = false, isDuplicate = false)
        }
    }

    fun onCategoryChange(newCategory: Category) {
        _uiState.value = _uiState.value.copy(category = newCategory, isDuplicate = false)
    }

    fun onNoteChange(newNote: String) {
        if (newNote.length <= 100) {
            _uiState.value = _uiState.value.copy(note = newNote)
        }
    }

    fun onReceiptSelected(uri: String?) {
        _uiState.value = _uiState.value.copy(selectedReceiptUri = uri)
    }

    fun onAddExpenseResultConsumed() {
        _uiState.value = _uiState.value.copy(isSuccess = false, isDuplicate = false)
    }

    fun addExpense() {
        val title = _uiState.value.title.trim()
        val amountStr = _uiState.value.amount.trim()
        val amountLong = amountStr.toLongOrNull()

        val hasError = title.isEmpty() || amountLong == null || amountLong <= 0
        if (hasError) {
            _uiState.value = _uiState.value.copy(
                titleError = title.isEmpty(),
                amountError = amountLong == null || amountLong <= 0
            )
            return
        }

        val amountInPaise = amountLong * 100

        val newExpense = Expense(
            title = title,
            amount = amountInPaise,
            category = _uiState.value.category,
            note = _uiState.value.note.takeIf { it.isNotBlank() },
            receiptUri = _uiState.value.selectedReceiptUri,
            createdAt = Instant.now()
        )

        viewModelScope.launch {
            val result = repository.add(newExpense)
            if (result.isSuccess) {
                // Clear form on success, keep category
                _uiState.value = EntryUiState(isSuccess = true)
            } else if (result.exceptionOrNull() is SQLiteConstraintException) {
                _uiState.value = _uiState.value.copy(isDuplicate = true)
            }
        }
    }
}
