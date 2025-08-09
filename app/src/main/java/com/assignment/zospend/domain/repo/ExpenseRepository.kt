package com.assignment.zospend.domain.repo

import com.assignment.zospend.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ExpenseRepository {
    val expenses: Flow<List<Expense>>
    fun expensesOn(date: LocalDate): Flow<List<Expense>>
    fun totalOn(date: LocalDate): Flow<Long>
    suspend fun add(expense: Expense): Result<Unit>
    suspend fun delete(id: String)
    suspend fun clearAll()
}
