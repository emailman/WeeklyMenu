# Weekly Menu

A modern Android meal planning app that helps you organize your weekly menu with category-based meal suggestions.

## Features

- **Rolling 7-Day Dashboard** - View your meal plan starting from today with full date display (e.g., "Wednesday, Jan 15"), followed by the next 6 days
- **Smart Meal Assignment** - Automatically assigns meals based on configurable food categories per day
- **8 Category Options**:
  - **Food Categories**: Beef, Chicken, Pork, Fish, Vegetarian
  - **Wildcard**: Random pick from all food categories
  - **Leftovers**: No meal assignment - use what's in the fridge
  - **Eat Out**: No meal assignment - enjoy a restaurant meal
- **Menu Item Library** - Manage your personal collection of meals with names, descriptions, and star ratings
- **Customizable Day Categories** - Configure which category is assigned to each day (changes save automatically)
- **Quick Regeneration** - Tap any day to get a new random meal suggestion from that day's category

## Screenshots

*Coming soon*

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material Design 3
- **Architecture**: MVVM with Repository pattern
- **Database**: Room (SQLite)
- **Navigation**: Jetpack Navigation Compose
- **Async**: Kotlin Coroutines & Flow

## Project Structure

```
app/src/main/java/edu/emailman/weeklymenu/
├── data/
│   ├── dao/           # Data Access Objects (Room)
│   ├── model/         # Entity classes
│   └── repository/    # Data access abstraction
└── ui/
    ├── components/    # Reusable UI components
    ├── navigation/    # Navigation setup
    ├── screens/       # App screens
    ├── theme/         # Material Design theming
    └── viewmodel/     # ViewModels
```

## Screens

| Screen | Description |
|--------|-------------|
| Dashboard | Rolling 7-day meal view starting from today, with full date display |
| Menu Items | Browse, add, edit, and delete meals organized by category |
| Settings | Configure category assignments for each day (auto-saves) |

## Requirements

- Android API 24+ (Android 7.0 Nougat)
- Android Studio Hedgehog or newer

## Getting Started

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run on an emulator or physical device

The app comes pre-populated with 25 sample menu items across 5 categories to get you started.

## Database Schema

- **menu_items** - Stores meal definitions (name, description, rating, category)
- **day_categories** - Maps each day of week to a food category
- **weekly_menu_entries** - Stores the current weekly menu selections

## License

*Add your license here*
