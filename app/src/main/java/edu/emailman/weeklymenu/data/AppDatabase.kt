package edu.emailman.weeklymenu.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import edu.emailman.weeklymenu.data.dao.DayCategoryDao
import edu.emailman.weeklymenu.data.dao.MenuItemDao
import edu.emailman.weeklymenu.data.dao.WeeklyMenuDao
import edu.emailman.weeklymenu.data.model.Category
import edu.emailman.weeklymenu.data.model.DayCategory
import edu.emailman.weeklymenu.data.model.MenuItem
import edu.emailman.weeklymenu.data.model.WeeklyMenuEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [MenuItem::class, DayCategory::class, WeeklyMenuEntry::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuItemDao(): MenuItemDao
    abstract fun dayCategoryDao(): DayCategoryDao
    abstract fun weeklyMenuDao(): WeeklyMenuDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "weekly_menu_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateSampleData(database)
                }
            }
        }

        private suspend fun populateSampleData(database: AppDatabase) {
            val menuItemDao = database.menuItemDao()
            val dayCategoryDao = database.dayCategoryDao()

            // Sample menu items for each category
            val sampleMenuItems = listOf(
                // Pork
                MenuItem(name = "Grilled Pork Chops", description = "Juicy bone-in pork chops with herbs", rating = 4, category = Category.PORK.name),
                MenuItem(name = "Pulled Pork", description = "Slow-cooked pulled pork with BBQ sauce", rating = 5, category = Category.PORK.name),
                MenuItem(name = "Pork Tenderloin", description = "Roasted tenderloin with garlic butter", rating = 4, category = Category.PORK.name),
                MenuItem(name = "Pork Stir Fry", description = "Asian-style pork with vegetables", rating = 3, category = Category.PORK.name),
                MenuItem(name = "Stuffed Pork Loin", description = "Pork loin stuffed with spinach and cheese", rating = 4, category = Category.PORK.name),

                // Fish
                MenuItem(name = "Grilled Salmon", description = "Fresh salmon with lemon dill sauce", rating = 5, category = Category.FISH.name),
                MenuItem(name = "Fish Tacos", description = "Crispy fish with cabbage slaw", rating = 4, category = Category.FISH.name),
                MenuItem(name = "Baked Cod", description = "Herb-crusted cod with vegetables", rating = 3, category = Category.FISH.name),
                MenuItem(name = "Shrimp Scampi", description = "Garlic butter shrimp over pasta", rating = 5, category = Category.FISH.name),
                MenuItem(name = "Tuna Steaks", description = "Seared ahi tuna with sesame crust", rating = 4, category = Category.FISH.name),

                // Vegetarian
                MenuItem(name = "Vegetable Stir Fry", description = "Mixed vegetables in ginger sauce", rating = 4, category = Category.VEGETARIAN.name),
                MenuItem(name = "Mushroom Risotto", description = "Creamy arborio rice with mixed mushrooms", rating = 5, category = Category.VEGETARIAN.name),
                MenuItem(name = "Eggplant Parmesan", description = "Breaded eggplant with marinara", rating = 4, category = Category.VEGETARIAN.name),
                MenuItem(name = "Veggie Burgers", description = "Homemade black bean burgers", rating = 3, category = Category.VEGETARIAN.name),
                MenuItem(name = "Stuffed Peppers", description = "Bell peppers with rice and beans", rating = 4, category = Category.VEGETARIAN.name),

                // Beef
                MenuItem(name = "Grilled Ribeye", description = "Prime ribeye with garlic butter", rating = 5, category = Category.BEEF.name),
                MenuItem(name = "Beef Tacos", description = "Seasoned ground beef with toppings", rating = 4, category = Category.BEEF.name),
                MenuItem(name = "Beef Stroganoff", description = "Tender beef in creamy mushroom sauce", rating = 4, category = Category.BEEF.name),
                MenuItem(name = "Meatloaf", description = "Classic meatloaf with glaze", rating = 3, category = Category.BEEF.name),
                MenuItem(name = "Beef Stew", description = "Hearty stew with potatoes and carrots", rating = 5, category = Category.BEEF.name),

                // Chicken
                MenuItem(name = "Grilled Chicken Breast", description = "Marinated chicken with herbs", rating = 4, category = Category.CHICKEN.name),
                MenuItem(name = "Chicken Parmesan", description = "Breaded chicken with marinara and cheese", rating = 5, category = Category.CHICKEN.name),
                MenuItem(name = "Lemon Herb Chicken", description = "Roasted chicken with lemon and rosemary", rating = 4, category = Category.CHICKEN.name),
                MenuItem(name = "Chicken Stir Fry", description = "Chicken with vegetables in teriyaki", rating = 3, category = Category.CHICKEN.name),
                MenuItem(name = "Chicken Fajitas", description = "Sizzling chicken with peppers and onions", rating = 4, category = Category.CHICKEN.name)
            )

            menuItemDao.insertMenuItems(sampleMenuItems)

            // Default day-category assignments
            val defaultDayCategories = listOf(
                DayCategory(dayOfWeek = 0, category = Category.BEEF.name),      // Sunday
                DayCategory(dayOfWeek = 1, category = Category.CHICKEN.name),   // Monday
                DayCategory(dayOfWeek = 2, category = Category.PORK.name),      // Tuesday
                DayCategory(dayOfWeek = 3, category = Category.VEGETARIAN.name),// Wednesday
                DayCategory(dayOfWeek = 4, category = Category.FISH.name),      // Thursday
                DayCategory(dayOfWeek = 5, category = Category.CHICKEN.name),   // Friday
                DayCategory(dayOfWeek = 6, category = Category.BEEF.name)       // Saturday
            )

            dayCategoryDao.insertDayCategories(defaultDayCategories)
        }
    }
}
