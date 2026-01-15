package edu.emailman.weeklymenu.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.emailman.weeklymenu.data.repository.MenuRepository
import edu.emailman.weeklymenu.ui.screens.DashboardScreen
import edu.emailman.weeklymenu.ui.screens.MenuItemEditScreen
import edu.emailman.weeklymenu.ui.screens.MenuItemListScreen
import edu.emailman.weeklymenu.ui.screens.SettingsScreen
import edu.emailman.weeklymenu.ui.viewmodel.DashboardViewModel
import edu.emailman.weeklymenu.ui.viewmodel.MenuItemViewModel
import edu.emailman.weeklymenu.ui.viewmodel.SettingsViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector?) {
    data object Dashboard : Screen("dashboard", "Weekly Menu", Icons.Default.Home)
    data object MenuItems : Screen("menu_items", "Menu Items", Icons.AutoMirrored.Filled.List)
    data object MenuItemEdit : Screen("menu_item_edit", "Edit", null)
    data object Settings : Screen("settings", "Daily Category", Icons.Default.Settings)
}

private val bottomNavItems = listOf(Screen.Dashboard, Screen.MenuItems, Screen.Settings)

@Composable
fun WeeklyMenuNavHost(
    repository: MenuRepository,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModel.Factory(repository)
    )
    val menuItemViewModel: MenuItemViewModel = viewModel(
        factory = MenuItemViewModel.Factory(repository)
    )
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.Factory(repository)
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            // Hide bottom nav on edit screen
            if (currentDestination?.route != Screen.MenuItemEdit.route) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon!!, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                // Reload dashboard when navigating back to it
                                if (screen.route == Screen.Dashboard.route) {
                                    dashboardViewModel.loadWeeklyMenu()
                                }
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(viewModel = dashboardViewModel)
            }
            composable(Screen.MenuItems.route) {
                MenuItemListScreen(
                    viewModel = menuItemViewModel,
                    onAddClick = {
                        navController.navigate(Screen.MenuItemEdit.route)
                    },
                    onItemClick = {
                        navController.navigate(Screen.MenuItemEdit.route)
                    },
                    onNavigateBack = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Dashboard.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.MenuItemEdit.route) {
                MenuItemEditScreen(
                    viewModel = menuItemViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = settingsViewModel,
                    onNavigateBack = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Dashboard.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
