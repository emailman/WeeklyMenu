package edu.emailman.weeklymenu.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.emailman.weeklymenu.data.model.DayCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface DayCategoryDao {
    @Query("SELECT * FROM day_categories ORDER BY dayOfWeek")
    fun getAllDayCategories(): Flow<List<DayCategory>>

    @Query("SELECT * FROM day_categories WHERE dayOfWeek = :dayOfWeek")
    suspend fun getDayCategoryByDay(dayOfWeek: Int): DayCategory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDayCategory(dayCategory: DayCategory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDayCategories(dayCategories: List<DayCategory>)
}
