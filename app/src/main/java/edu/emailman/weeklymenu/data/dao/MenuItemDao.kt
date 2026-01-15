package edu.emailman.weeklymenu.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import edu.emailman.weeklymenu.data.model.MenuItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuItemDao {
    @Query("SELECT * FROM menu_items ORDER BY category, name")
    fun getAllMenuItems(): Flow<List<MenuItem>>

    @Query("SELECT * FROM menu_items")
    suspend fun getAllMenuItemsList(): List<MenuItem>

    @Query("SELECT * FROM menu_items WHERE category = :category")
    fun getMenuItemsByCategory(category: String): Flow<List<MenuItem>>

    @Query("SELECT * FROM menu_items WHERE category = :category")
    suspend fun getMenuItemsByCategoryList(category: String): List<MenuItem>

    @Query("SELECT * FROM menu_items WHERE id = :id")
    suspend fun getMenuItemById(id: Long): MenuItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItem(menuItem: MenuItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItems(menuItems: List<MenuItem>)

    @Update
    suspend fun updateMenuItem(menuItem: MenuItem)

    @Delete
    suspend fun deleteMenuItem(menuItem: MenuItem)

    @Query("UPDATE menu_items SET lastMadeDate = :date WHERE id = :id")
    suspend fun updateLastMadeDate(id: Long, date: Long)
}
