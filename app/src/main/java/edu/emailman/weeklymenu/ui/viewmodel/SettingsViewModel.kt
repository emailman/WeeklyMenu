package edu.emailman.weeklymenu.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.emailman.weeklymenu.data.model.Category
import edu.emailman.weeklymenu.data.model.DayCategory
import edu.emailman.weeklymenu.data.repository.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class DayCategorySetting(
    val dayOfWeek: Int,
    val dayName: String,
    val category: Category
)

data class SettingsUiState(
    val dayCategories: List<DayCategorySetting> = emptyList(),
    val isLoading: Boolean = true
)

class SettingsViewModel(private val repository: MenuRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val dayNames = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val dayCategories = repository.getAllDayCategories().first()
            val settings = (0..6).map { dayOfWeek ->
                val existingCategory = dayCategories.find { it.dayOfWeek == dayOfWeek }
                DayCategorySetting(
                    dayOfWeek = dayOfWeek,
                    dayName = dayNames[dayOfWeek],
                    category = Category.fromString(existingCategory?.category ?: Category.CHICKEN.name)
                )
            }

            _uiState.value = SettingsUiState(
                dayCategories = settings,
                isLoading = false
            )
        }
    }

    fun updateDayCategory(dayOfWeek: Int, category: Category) {
        val currentSettings = _uiState.value.dayCategories.toMutableList()
        val index = currentSettings.indexOfFirst { it.dayOfWeek == dayOfWeek }
        if (index != -1) {
            currentSettings[index] = currentSettings[index].copy(category = category)
            _uiState.value = _uiState.value.copy(dayCategories = currentSettings)

            // Save immediately to database
            viewModelScope.launch {
                repository.updateDayCategory(
                    DayCategory(dayOfWeek = dayOfWeek, category = category.name)
                )
            }
        }
    }

    class Factory(private val repository: MenuRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                return SettingsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
