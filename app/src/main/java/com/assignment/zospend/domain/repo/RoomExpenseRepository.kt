package com.assignment.zospend.domain.repo

import android.database.sqlite.SQLiteConstraintException
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
            expenseDao.insertExpense(expense)
            Result.success(Unit)
        } catch (e: SQLiteConstraintException) {
            Result.failure(e)
        }
    }

    override suspend fun update(expense: Expense): Result<Unit> {
        return try {
            expenseDao.updateExpense(expense)
            Result.success(Unit)
        } catch (e: SQLiteConstraintException) {
            Result.failure(e)
        }
    }

    suspend fun getExpenseById(id: Long) = expenseDao.getExpenseById(id)
}
