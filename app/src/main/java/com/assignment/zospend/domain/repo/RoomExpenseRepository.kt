package com.assignment.zospend.domain.repo

import com.assignment.zospend.data.local.Expense
import com.assignment.zospend.data.local.ExpenseDao
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class RoomExpenseRepository(private val expenseDao: ExpenseDao) : ExpenseRepository {
    override fun allExpenses() = expenseDao.getAllExpenses()

    override fun expensesOn(date: LocalDate) =
        expenseDao.getAllExpenses().map { list ->
            list.filter { it.getCreatedAtLocalDate() == date }
        }

    override suspend fun add(expense: Expense): Result<Unit> {
        return try {
            expenseDao.upsertExpense(expense)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
