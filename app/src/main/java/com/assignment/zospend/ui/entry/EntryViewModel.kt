package com.assignment.zospend.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.zospend.data.ServiceLocator
import com.assignment.zospend.domain.model.Category
import com.assignment.zospend.domain.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

data class EntryUiState(
    val title: String = "",
    val amount: String = "",
    val category: Category = Category.FOOD,
    val note: String? = null,
    val receiptUri: String? = null,
    val titleError: Boolean = false,
    val amountError: Boolean = false,
    val addExpenseResult: Result<Unit>? = null
)

class EntryViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(EntryUiState())
    val uiState = _uiState.asStateFlow()

    val repository = ServiceLocator.provideRepository()
    val totalSpentToday = repository.totalOn(LocalDate.now())
        .map { it / 100.0 } // Convert paise to rupees

    fun onTitleChange(newTitle: String) {
        _uiState.value = _uiState.value.copy(title = newTitle, titleError = false)
    }

    fun onAmountChange(newAmount: String) {
        _uiState.value = _uiState.value.copy(amount = newAmount, amountError = false)
    }

    fun onCategoryChange(newCategory: Category) {
        _uiState.value = _uiState.value.copy(category = newCategory)
    }

    fun onNoteChange(newNote: String) {
        _uiState.value = _uiState.value.copy(note = newNote)
    }

    fun onReceiptSelected(uri: String?) {
        _uiState.value = _uiState.value.copy(receiptUri = uri)
    }

    fun onAddExpenseResultConsumed() {
        _uiState.value = _uiState.value.copy(addExpenseResult = null)
    }

    fun addExpense() {
        val title = _uiState.value.title.trim()
        val amountStr = _uiState.value.amount.trim()

        val hasError = title.isEmpty() || amountStr.toDoubleOrNull()?.let { it <= 0 } ?: true
        if (hasError) {
            _uiState.value = _uiState.value.copy(
                titleError = title.isEmpty(),
                amountError = amountStr.toDoubleOrNull()?.let { it <= 0 } ?: true
            )
            return
        }

        val amountInPaise = (amountStr.toDouble() * 100).toLong()

        val newExpense = Expense(
            title = title,
            amount = amountInPaise,
            category = _uiState.value.category,
            note = _uiState.value.note?.takeIf { it.isNotBlank() },
            receiptUri = _uiState.value.receiptUri
        )

        viewModelScope.launch {
            val result = repository.add(newExpense)
            _uiState.value = _uiState.value.copy(addExpenseResult = result)
            if (result.isSuccess) {
                // Clear form on success
                _uiState.value = EntryUiState(category = _uiState.value.category) // Keep category
            }
        }
    }
}
