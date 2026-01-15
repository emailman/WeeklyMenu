package edu.emailman.weeklymenu.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.emailman.weeklymenu.data.model.WeeklyMenuEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface WeeklyMenuDao {
    @Query("SELECT * FROM weekly_menu_entries WHERE weekStartDate = :weekStartDate ORDER BY dayOfWeek")
    fun getEntriesForWeek(weekStartDate: Long): Flow<List<WeeklyMenuEntry>>

    @Query("SELECT * FROM weekly_menu_entries WHERE weekStartDate = :weekStartDate ORDER BY dayOfWeek")
    suspend fun getEntriesForWeekList(weekStartDate: Long): List<WeeklyMenuEntry>

    @Query("SELECT * FROM weekly_menu_entries WHERE weekStartDate = :weekStartDate AND dayOfWeek = :dayOfWeek")
    suspend fun getEntryForDay(weekStartDate: Long, dayOfWeek: Int): WeeklyMenuEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: WeeklyMenuEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(entries: List<WeeklyMenuEntry>)

    @Query("DELETE FROM weekly_menu_entries WHERE weekStartDate = :weekStartDate AND dayOfWeek = :dayOfWeek")
    suspend fun deleteEntryForDay(weekStartDate: Long, dayOfWeek: Int)
}
