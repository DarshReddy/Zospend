package com.assignment.zospend.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.assignment.zospend.domain.model.Category
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Long, // Stored in paise
    val category: Category,
    val note: String?,
    val receiptUri: String?,
    val createdAt: Instant
) {
    fun getCreatedAtLocalDate(zoneId: ZoneId = ZoneId.systemDefault()): LocalDate {
        return createdAt.atZone(zoneId).toLocalDate()
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): Long? {
        return date?.toEpochMilli()
    }
}
