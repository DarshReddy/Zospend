
package com.assignment.zospend.data

import android.content.Context
import com.assignment.zospend.data.local.AppDatabase
import com.assignment.zospend.domain.repo.ExpenseRepository
import com.assignment.zospend.domain.repo.RoomExpenseRepository

object ServiceLocator {

    private var database: AppDatabase? = null

    private val repository: ExpenseRepository by lazy {
        RoomExpenseRepository(database!!.expenseDao())
    }

    fun provideRepository(context: Context): ExpenseRepository {
        database = AppDatabase.getDatabase(context)
        return repository
    }
}
