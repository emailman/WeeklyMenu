package edu.emailman.weeklymenu.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val rating: Int,
    val category: String,
    val lastMadeDate: Long? = null
)
