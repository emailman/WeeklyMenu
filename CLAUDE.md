# Project Context for Claude

## Project Overview
Weekly Menu is an Android meal planning app built with Kotlin and Jetpack Compose. It helps users organize weekly meals by automatically assigning dishes based on food categories (Beef, Chicken, Pork, Fish, Vegetarian) to specific days.

## Tech Stack
- Kotlin, Android API 24-36
- Jetpack Compose + Material Design 3
- MVVM architecture with Repository pattern
- Room database for persistence
- Jetpack Navigation Compose
- Kotlin Coroutines & Flow

## Key Directories
- `app/src/main/java/edu/emailman/weeklymenu/data/` - Database, DAOs, models, repository
- `app/src/main/java/edu/emailman/weeklymenu/ui/screens/` - DashboardScreen, MenuItemListScreen, MenuItemEditScreen, SettingsScreen
- `app/src/main/java/edu/emailman/weeklymenu/ui/viewmodel/` - ViewModels for each screen
- `app/src/main/java/edu/emailman/weeklymenu/ui/components/` - DayCard, CategoryChip, StarRating

## Database Tables
- `menu_items` - Meal definitions (name, description, rating 1-5, category)
- `day_categories` - Maps day of week to food category
- `weekly_menu_entries` - Current week's meal selections

## Main Features
- Dashboard: Rolling 7-day view starting from today with full date display (e.g., "Wednesday, Jan 15"), tap to regenerate meal
- Menu Items: CRUD operations, swipe-to-delete, star ratings
- Settings: Configure category per day of week

## Sample Data
App initializes with 25 pre-populated menu items across 5 categories.

## Current State
- Core functionality complete
- Dashboard shows rolling 7-day view starting from today
- Handles week boundaries correctly (when 7-day window spans two calendar weeks)
- No known bugs or pending tasks
