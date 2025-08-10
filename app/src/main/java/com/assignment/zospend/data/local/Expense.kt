package com.assignment.zospend.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.assignment.zospend.domain.model.Category
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Entity(
    tableName = "expenses",
    indices = [Index(value = ["amount", "title", "category"], unique = true)]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Long, // Stored amount x100 for paise,cent etc
    val category: Category,
    val note: String?,
    val receiptUri: String?,
    val createdAt: Instant,
    val isMock: Boolean = false
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
