package edu.emailman.weeklymenu.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "weekly_menu_entries",
    foreignKeys = [
        ForeignKey(
            entity = MenuItem::class,
            parentColumns = ["id"],
            childColumns = ["menuItemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("menuItemId")]
)
data class WeeklyMenuEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val weekStartDate: Long,
    val dayOfWeek: Int,
    val menuItemId: Long
)
