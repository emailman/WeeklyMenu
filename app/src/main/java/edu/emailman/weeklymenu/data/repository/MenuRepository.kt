package edu.emailman.weeklymenu.data.repository

import edu.emailman.weeklymenu.data.dao.DayCategoryDao
import edu.emailman.weeklymenu.data.dao.MenuItemDao
import edu.emailman.weeklymenu.data.dao.WeeklyMenuDao
import edu.emailman.weeklymenu.data.model.DayCategory
import edu.emailman.weeklymenu.data.model.MenuItem
import edu.emailman.weeklymenu.data.model.WeeklyMenuEntry
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class MenuRepository(
    private val menuItemDao: MenuItemDao,
    private val dayCategoryDao: DayCategoryDao,
    private val weeklyMenuDao: WeeklyMenuDao
) {
    // Menu Items
    fun getAllMenuItems(): Flow<List<MenuItem>> = menuItemDao.getAllMenuItems()

    fun getMenuItemsByCategory(category: String): Flow<List<MenuItem>> =
        menuItemDao.getMenuItemsByCategory(category)

    suspend fun getMenuItemById(id: Long): MenuItem? = menuItemDao.getMenuItemById(id)

    suspend fun insertMenuItem(menuItem: MenuItem): Long = menuItemDao.insertMenuItem(menuItem)

    suspend fun updateMenuItem(menuItem: MenuItem) = menuItemDao.updateMenuItem(menuItem)

    suspend fun deleteMenuItem(menuItem: MenuItem) = menuItemDao.deleteMenuItem(menuItem)

    suspend fun updateLastMadeDate(id: Long, date: Long) = menuItemDao.updateLastMadeDate(id, date)

    // Day Categories
    fun getAllDayCategories(): Flow<List<DayCategory>> = dayCategoryDao.getAllDayCategories()

    suspend fun getDayCategoryByDay(dayOfWeek: Int): DayCategory? =
        dayCategoryDao.getDayCategoryByDay(dayOfWeek)

    suspend fun updateDayCategory(dayCategory: DayCategory) =
        dayCategoryDao.insertDayCategory(dayCategory)

    suspend fun updateDayCategories(dayCategories: List<DayCategory>) =
        dayCategoryDao.insertDayCategories(dayCategories)

    // Weekly Menu
    fun getEntriesForWeek(weekStartDate: Long): Flow<List<WeeklyMenuEntry>> =
        weeklyMenuDao.getEntriesForWeek(weekStartDate)

    suspend fun getEntriesForWeekList(weekStartDate: Long): List<WeeklyMenuEntry> =
        weeklyMenuDao.getEntriesForWeekList(weekStartDate)

    suspend fun getEntryForDay(weekStartDate: Long, dayOfWeek: Int): WeeklyMenuEntry? =
        weeklyMenuDao.getEntryForDay(weekStartDate, dayOfWeek)

    suspend fun saveWeeklyEntry(entry: WeeklyMenuEntry) = weeklyMenuDao.insertEntry(entry)

    suspend fun saveWeeklyEntries(entries: List<WeeklyMenuEntry>) =
        weeklyMenuDao.insertEntries(entries)

    // Random selection
    suspend fun getRandomMenuItemForCategory(category: String): MenuItem? {
        val items = menuItemDao.getMenuItemsByCategoryList(category)
        return if (items.isNotEmpty()) items.random() else null
    }

    suspend fun getRandomMenuItemFromAnyCategory(): MenuItem? {
        val items = menuItemDao.getAllMenuItemsList()
        return if (items.isNotEmpty()) items.random() else null
    }

    // Utility
    fun getWeekStartDate(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun getWeekStartDateForDate(dateMillis: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateMillis
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
