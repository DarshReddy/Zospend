package com.assignment.zospend.domain.repo

import com.assignment.zospend.data.local.Expense
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ExpenseRepository {
    fun allExpenses(): Flow<List<Expense>>
    fun expensesOn(date: LocalDate): Flow<List<Expense>>
    suspend fun add(expense: Expense): Result<Unit>
    suspend fun update(expense: Expense): Result<Unit>
}
