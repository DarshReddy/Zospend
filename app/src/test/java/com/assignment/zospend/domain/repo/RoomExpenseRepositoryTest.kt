package com.assignment.zospend.domain.repo

import com.assignment.zospend.data.local.Expense
import com.assignment.zospend.data.local.ExpenseDao
import com.assignment.zospend.domain.model.Category
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class RoomExpenseRepositoryTest {

    private lateinit var expenseDao: ExpenseDao
    private lateinit var repository: RoomExpenseRepository

    @Before
    fun setUp() {
        expenseDao = mock()
        repository = RoomExpenseRepository(expenseDao)
    }

    @Test
    fun `expensesOn successfully adds a new expense`() = runTest {
        val expenseToday = Expense(
            id = 1,
            title = "Test Exp 1",
            amount = 1000,
            Category.FOOD,
            "Lunch",
            createdAt = Instant.now(),
            receiptUri = null
        )
        val expenseYesterday = Expense(
            id = 2,
            title = "Test Exp 2",
            amount = 1000,
            Category.FOOD,
            "Lunch",
            createdAt = Instant.now().minus(1, ChronoUnit.DAYS),
            receiptUri = null
        )
        whenever(expenseDao.getAllExpenses()).thenReturn(
            flowOf(
                listOf(
                    expenseToday,
                    expenseYesterday
                )
            )
        )
        val expenses = repository.expensesOn(LocalDate.now()).first()
        assertEquals(1, expenses.size)
        assertEquals(expenseToday, expenses[0])
    }
}
