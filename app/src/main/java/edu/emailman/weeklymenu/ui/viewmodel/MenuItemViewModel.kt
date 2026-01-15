package edu.emailman.weeklymenu.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.emailman.weeklymenu.data.model.Category
import edu.emailman.weeklymenu.data.model.MenuItem
import edu.emailman.weeklymenu.data.repository.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MenuItemEditState(
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val rating: Int = 3,
    val category: Category = Category.CHICKEN,
    val isEditing: Boolean = false
)

class MenuItemViewModel(private val repository: MenuRepository) : ViewModel() {

    val menuItems: StateFlow<List<MenuItem>> = repository.getAllMenuItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _editState = MutableStateFlow(MenuItemEditState())
    val editState: StateFlow<MenuItemEditState> = _editState.asStateFlow()

    fun startNewItem() {
        _editState.value = MenuItemEditState()
    }

    fun startEdit(menuItem: MenuItem) {
        _editState.value = MenuItemEditState(
            id = menuItem.id,
            name = menuItem.name,
            description = menuItem.description,
            rating = menuItem.rating,
            category = Category.fromString(menuItem.category),
            isEditing = true
        )
    }

    fun updateName(name: String) {
        _editState.value = _editState.value.copy(name = name)
    }

    fun updateDescription(description: String) {
        _editState.value = _editState.value.copy(description = description)
    }

    fun updateRating(rating: Int) {
        _editState.value = _editState.value.copy(rating = rating)
    }

    fun updateCategory(category: Category) {
        _editState.value = _editState.value.copy(category = category)
    }

    fun saveMenuItem(onComplete: () -> Unit) {
        viewModelScope.launch {
            val state = _editState.value
            val menuItem = MenuItem(
                id = if (state.isEditing) state.id else 0,
                name = state.name,
                description = state.description,
                rating = state.rating,
                category = state.category.name
            )

            if (state.isEditing) {
                repository.updateMenuItem(menuItem)
            } else {
                repository.insertMenuItem(menuItem)
            }

            onComplete()
        }
    }

    fun deleteMenuItem(menuItem: MenuItem) {
        viewModelScope.launch {
            repository.deleteMenuItem(menuItem)
        }
    }

    class Factory(private val repository: MenuRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MenuItemViewModel::class.java)) {
                return MenuItemViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
