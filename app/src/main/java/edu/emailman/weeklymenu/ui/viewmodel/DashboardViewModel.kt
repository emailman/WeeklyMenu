package edu.emailman.weeklymenu.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.emailman.weeklymenu.data.model.MenuItem
import edu.emailman.weeklymenu.data.model.WeeklyMenuEntry
import edu.emailman.weeklymenu.data.repository.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class DayMenuState(
    val dayOfWeek: Int,
    val displayDate: String,
    val category: String,
    val menuItem: MenuItem?
)

data class DashboardUiState(
    val weeklyMenu: List<DayMenuState> = emptyList(),
    val isLoading: Boolean = true
)

class DashboardViewModel(private val repository: MenuRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val dateFormat = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())

    init {
        loadWeeklyMenu()
    }

    fun loadWeeklyMenu() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val dayCategories = repository.getAllDayCategories().first()

            // Pre-fetch entries for current and next week to handle week boundaries
            val currentWeekStart = repository.getWeekStartDate()
            val nextWeekStart = currentWeekStart + 7 * 24 * 60 * 60 * 1000L
            val currentWeekEntries = repository.getEntriesForWeekList(currentWeekStart)
            val nextWeekEntries = repository.getEntriesForWeekList(nextWeekStart)

            val weeklyMenu = mutableListOf<DayMenuState>()
            val calendar = Calendar.getInstance()

            for (dayOffset in 0..6) {
                calendar.timeInMillis = System.currentTimeMillis()
                calendar.add(Calendar.DAY_OF_YEAR, dayOffset)

                // Get the actual day of week (Calendar uses 1=Sunday, 2=Monday, etc.)
                // Convert to our 0-indexed format (0=Sunday, 1=Monday, etc.)
                val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
                val displayDate = dateFormat.format(calendar.time)

                // Determine which week this day belongs to
                val weekStartDate = repository.getWeekStartDateForDate(calendar.timeInMillis)
                val existingEntries = if (weekStartDate == currentWeekStart) currentWeekEntries else nextWeekEntries

                val dayCategory = dayCategories.find { it.dayOfWeek == dayOfWeek }
                val category = dayCategory?.category ?: "CHICKEN"

                val existingEntry = existingEntries.find { it.dayOfWeek == dayOfWeek }
                val menuItem = if (existingEntry != null) {
                    repository.getMenuItemById(existingEntry.menuItemId)
                } else {
                    val randomItem = repository.getRandomMenuItemForCategory(category)
                    if (randomItem != null) {
                        repository.saveWeeklyEntry(
                            WeeklyMenuEntry(
                                weekStartDate = weekStartDate,
                                dayOfWeek = dayOfWeek,
                                menuItemId = randomItem.id
                            )
                        )
                    }
                    randomItem
                }

                weeklyMenu.add(
                    DayMenuState(
                        dayOfWeek = dayOfWeek,
                        displayDate = displayDate,
                        category = category,
                        menuItem = menuItem
                    )
                )
            }

            _uiState.value = DashboardUiState(
                weeklyMenu = weeklyMenu,
                isLoading = false
            )
        }
    }

    fun regenerateDay(dayIndex: Int) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, dayIndex)

            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
            val weekStartDate = repository.getWeekStartDateForDate(calendar.timeInMillis)

            val dayCategories = repository.getAllDayCategories().first()
            val dayCategory = dayCategories.find { it.dayOfWeek == dayOfWeek }
            val category = dayCategory?.category ?: "CHICKEN"

            val randomItem = repository.getRandomMenuItemForCategory(category)
            if (randomItem != null) {
                repository.saveWeeklyEntry(
                    WeeklyMenuEntry(
                        weekStartDate = weekStartDate,
                        dayOfWeek = dayOfWeek,
                        menuItemId = randomItem.id
                    )
                )

                val currentMenu = _uiState.value.weeklyMenu.toMutableList()
                if (dayIndex in currentMenu.indices) {
                    currentMenu[dayIndex] = currentMenu[dayIndex].copy(menuItem = randomItem)
                    _uiState.value = _uiState.value.copy(weeklyMenu = currentMenu)
                }
            }
        }
    }

    class Factory(private val repository: MenuRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                return DashboardViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
