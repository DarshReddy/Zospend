package com.assignment.zospend.data

import com.assignment.zospend.domain.repo.ExpenseRepository
import com.assignment.zospend.domain.repo.InMemoryExpenseRepository

object ServiceLocator {

    private val repository: ExpenseRepository by lazy {
        InMemoryExpenseRepository()
    }

    fun provideRepository(): ExpenseRepository {
        return repository
    }
}
