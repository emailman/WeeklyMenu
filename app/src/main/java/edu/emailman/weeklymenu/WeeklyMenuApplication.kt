package edu.emailman.weeklymenu

import android.app.Application
import edu.emailman.weeklymenu.data.AppDatabase
import edu.emailman.weeklymenu.data.repository.MenuRepository

class WeeklyMenuApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy {
        MenuRepository(
            menuItemDao = database.menuItemDao(),
            dayCategoryDao = database.dayCategoryDao(),
            weeklyMenuDao = database.weeklyMenuDao()
        )
    }
}
