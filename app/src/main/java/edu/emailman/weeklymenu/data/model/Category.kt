package edu.emailman.weeklymenu.data.model

enum class Category(val displayName: String) {
    PORK("Pork"),
    FISH("Fish"),
    VEGETARIAN("Vegetarian"),
    BEEF("Beef"),
    CHICKEN("Chicken"),
    WILDCARD("Wildcard"),
    LEFTOVERS("Leftovers"),
    EAT_OUT("Eat Out");

    companion object {
        fun fromString(value: String): Category {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: CHICKEN
        }

        // Categories that have menu items to pick from
        val foodCategories: List<Category>
            get() = listOf(PORK, FISH, VEGETARIAN, BEEF, CHICKEN)
    }
}
