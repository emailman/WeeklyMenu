package edu.emailman.weeklymenu.data.model

enum class Category(val displayName: String) {
    PORK("Pork"),
    FISH("Fish"),
    VEGETARIAN("Vegetarian"),
    BEEF("Beef"),
    CHICKEN("Chicken");

    companion object {
        fun fromString(value: String): Category {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: CHICKEN
        }
    }
}
