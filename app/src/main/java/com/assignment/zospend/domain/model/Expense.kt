package com.assignment.zospend.domain.model

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val amount: Long, // Stored in paise
    val category: Category,
    val note: String?,
    val receiptUri: String?,
    val createdAt: Instant = Instant.now()
) {
    fun getCreatedAtLocalDate(zoneId: ZoneId = ZoneId.systemDefault()): LocalDate {
        return createdAt.atZone(zoneId).toLocalDate()
    }
}
