package com.assignment.zospend.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.zospend.data.ServiceLocator
import com.assignment.zospend.domain.model.Category
import com.assignment.zospend.domain.model.Expense
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

data class ExpenseListUiState(
    val expenses: Map<Category, List<Expense>> = emptyMap(),
    val selectedDate: LocalDate = LocalDate.now(),
    val isGroupedByCategory: Boolean = false,
    val totalAmount: Long = 0,
    val totalCount: Int = 0
)

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseListViewModel() : ViewModel() {
    val repository = ServiceLocator.provideRepository()
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    private val _isGroupedByCategory = MutableStateFlow(false)

    private val _uiState = MutableStateFlow(ExpenseListUiState())
    val uiState: StateFlow<ExpenseListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                _selectedDate.flatMapLatest { date -> repository.expensesOn(date) },
                _isGroupedByCategory
            ) { expenses, isGrouped ->
                val totalAmount = expenses.sumOf { it.amount }
                val groupedExpenses = if (isGrouped) {
                    expenses.groupBy { it.category }
                } else {
                    // When not grouped, we can put all items under a "dummy" category
                    // or handle it differently in the UI. Grouping is easier.
                    mapOf(Category.FOOD to expenses.sortedByDescending { it.createdAt })
                }
                ExpenseListUiState(
                    expenses = groupedExpenses,
                    selectedDate = _selectedDate.value,
                    isGroupedByCategory = isGrouped,
                    totalAmount = totalAmount,
                    totalCount = expenses.size
                )
            }.collect {
                _uiState.value = it
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    fun onGroupingToggled(isGrouped: Boolean) {
        _isGroupedByCategory.value = isGrouped
    }
}
