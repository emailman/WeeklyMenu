package edu.emailman.weeklymenu.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "day_categories")
data class DayCategory(
    @PrimaryKey
    val dayOfWeek: Int,
    val category: String
)
