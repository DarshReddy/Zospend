package com.assignment.zospend.domain.repo

import com.assignment.zospend.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class InMemoryExpenseRepository : ExpenseRepository {

    private val expensesFlow = MutableStateFlow<List<Expense>>(emptyList())

    override val expenses: Flow<List<Expense>> = expensesFlow

    override fun expensesOn(date: LocalDate): Flow<List<Expense>> {
        return expenses.map { list ->
            list.filter { it.getCreatedAtLocalDate() == date }
        }
    }

    override fun totalOn(date: LocalDate): Flow<Long> {
        return expensesOn(date).map { list ->
            list.sumOf { it.amount }
        }
    }

    override suspend fun add(expense: Expense): Result<Unit> {
        val existing = expensesFlow.value
        val isDuplicate = existing.any {
            val timeDifference = ChronoUnit.MINUTES.between(it.createdAt, expense.createdAt)
            it.title.trim().equals(expense.title.trim(), ignoreCase = true) &&
                    it.amount == expense.amount &&
                    it.getCreatedAtLocalDate() == expense.getCreatedAtLocalDate() &&
                    timeDifference in -5..5
        }

        return if (isDuplicate) {
            Result.failure(Exception("Duplicate expense"))
        } else {
            expensesFlow.value = existing + expense
            Result.success(Unit)
        }
    }

    override suspend fun delete(id: String) {
        expensesFlow.value = expensesFlow.value.filterNot { it.id == id }
    }

    override suspend fun clearAll() {
        expensesFlow.value = emptyList()
    }
}
