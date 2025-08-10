package com.assignment.zospend.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.zospend.data.ServiceLocator
import com.assignment.zospend.data.local.Expense
import com.assignment.zospend.data.mock.MockExpense
import com.assignment.zospend.domain.model.Category
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class ExpenseListUiState(
    val expenses: Map<Category?, List<Expense>> = emptyMap(),
    val selectedDate: LocalDate = LocalDate.now(),
    val isGroupedByCategory: Boolean = false,
    val totalAmount: Long = 0,
    val totalCount: Int = 0,
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ServiceLocator.provideRepository(application)
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    private val _isGroupedByCategory = MutableStateFlow(true)

    private val _uiState = MutableStateFlow(ExpenseListUiState())
    val uiState: StateFlow<ExpenseListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                _selectedDate.flatMapLatest { date ->
                    if (LocalDate.now() == date) {
                        repository.expensesOn(date)
                    } else {
                        flow {
                            emit(MockExpense.generateMocksForDate(date))
                        }
                    }
                },
                _isGroupedByCategory
            ) { expenses, isGrouped ->
                val totalAmount = expenses.sumOf { it.amount }
                val groupedExpenses = if (expenses.isEmpty()) {
                    emptyMap()
                } else if (isGrouped) {
                    expenses.groupBy { it.category }
                } else {
                    mapOf(null to expenses.sortedByDescending { it.createdAt })
                }
                ExpenseListUiState(
                    expenses = groupedExpenses as Map<Category?, List<Expense>>,
                    selectedDate = _selectedDate.value,
                    isGroupedByCategory = isGrouped,
                    totalAmount = totalAmount,
                    totalCount = expenses.size,
                    isLoading = false
                )
            }.collect {
                _uiState.value = it
            }
        }
    }

    fun onPreviousDayClicked() {
        _selectedDate.value = _selectedDate.value.minusDays(1)
    }

    fun onNextDayClicked() {
        _selectedDate.value = _selectedDate.value.plusDays(1)
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }
}
