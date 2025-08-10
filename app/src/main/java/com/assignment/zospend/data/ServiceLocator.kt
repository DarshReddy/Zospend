package com.assignment.zospend.data

import android.content.Context
import com.assignment.zospend.data.local.AppDatabase
import com.assignment.zospend.data.mock.MockExpense
import com.assignment.zospend.domain.repo.ExpenseRepository
import com.assignment.zospend.domain.repo.RoomExpenseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ServiceLocator {

    private var database: AppDatabase? = null

    private val repository: ExpenseRepository by lazy {
        val dao = database!!.expenseDao()
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAllMockExpenses()
            dao.insertExpenses(MockExpense.generateMockExpenses())
        }
        RoomExpenseRepository(dao)
    }

    fun provideRepository(context: Context): ExpenseRepository {
        database = AppDatabase.getDatabase(context)
        return repository
    }
}
