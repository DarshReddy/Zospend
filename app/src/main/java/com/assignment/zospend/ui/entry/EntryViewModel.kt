
package com.assignment.zospend.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.zospend.domain.model.Category
import com.assignment.zospend.domain.model.Expense
import com.assignment.zospend.domain.repo.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EntryUiState(
    val title: String = "",
    val amount: String = "",
    val category: Category = Category.FOOD,
    val note: String = "",
    val selectedReceiptUri: String? = null,
    val titleError: Boolean = false,
    val amountError: Boolean = false,
    val addExpenseResult: Result<Unit>? = null
)

class EntryViewModel(private val repository: ExpenseRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(EntryUiState())
    val uiState = _uiState.asStateFlow()

    fun onTitleChange(newTitle: String) {
        _uiState.value = _uiState.value.copy(title = newTitle, titleError = false)
    }

    fun onAmountChange(newAmount: String) {
        // Allow only digits
        if (newAmount.all { it.isDigit() }) {
            _uiState.value = _uiState.value.copy(amount = newAmount, amountError = false)
        }
    }

    fun onCategoryChange(newCategory: Category) {
        _uiState.value = _uiState.value.copy(category = newCategory)
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
        _uiState.value = _uiState.value.copy(addExpenseResult = null)
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
            receiptUri = _uiState.value.selectedReceiptUri
        )

        viewModelScope.launch {
            val result = repository.add(newExpense)
            _uiState.value = _uiState.value.copy(addExpenseResult = result)
            if (result.isSuccess) {
                // Clear form on success, keep category
                _uiState.value = EntryUiState(category = _uiState.value.category)
            }
        }
    }
}
