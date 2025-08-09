package com.assignment.zospend.ui.entry

import android.app.Application
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.zospend.data.ServiceLocator
import com.assignment.zospend.data.local.Expense
import com.assignment.zospend.domain.model.Category
import com.assignment.zospend.domain.repo.RoomExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant

data class EntryUiState(
    val id: Long? = null,
    val title: String = "",
    val amount: String = "",
    val category: Category = Category.FOOD,
    val note: String = "",
    val selectedReceiptUri: String? = null,
    val titleError: Boolean = false,
    val amountError: Boolean = false,
    val isDuplicate: Boolean = false,
    val isSuccess: Boolean = false,
    val isEditMode: Boolean = false
)

class EntryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ServiceLocator.provideRepository(application) as RoomExpenseRepository
    private val _uiState = MutableStateFlow(EntryUiState())
    val uiState = _uiState.asStateFlow()
    private val contentResolver = application.contentResolver

    fun loadExpense(expenseId: Long) {
        if (expenseId == -1L) return
        viewModelScope.launch {
            val expense = repository.getExpenseById(expenseId)
            if (expense != null) {
                _uiState.value = EntryUiState(
                    id = expense.id,
                    title = expense.title,
                    amount = (expense.amount / 100).toString(),
                    category = expense.category,
                    note = expense.note ?: "",
                    selectedReceiptUri = expense.receiptUri,
                    isEditMode = true
                )
            }
        }
    }

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

    fun onReceiptSelected(uriString: String?) {
        uriString?.let {
            val uri = it.toUri()
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            _uiState.value = _uiState.value.copy(selectedReceiptUri = uri.toString())
        }
    }

    fun saveExpense() {
        if (_uiState.value.isEditMode) {
            updateExpense()
        } else {
            addExpense()
        }
    }

    private fun addExpense() {
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

    private fun updateExpense() {
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

        val updatedExpense = Expense(
            id = _uiState.value.id!!,
            title = title,
            amount = amountInPaise,
            category = _uiState.value.category,
            note = _uiState.value.note.takeIf { it.isNotBlank() },
            receiptUri = _uiState.value.selectedReceiptUri,
            createdAt = Instant.now() // Note: you might want to preserve the original creation date
        )

        viewModelScope.launch {
            val result = repository.update(updatedExpense)
            if (result.isSuccess) {
                _uiState.value = EntryUiState(isSuccess = true)
            } else if (result.exceptionOrNull() is SQLiteConstraintException) {
                _uiState.value = _uiState.value.copy(isDuplicate = true)
            }
        }
    }
}
