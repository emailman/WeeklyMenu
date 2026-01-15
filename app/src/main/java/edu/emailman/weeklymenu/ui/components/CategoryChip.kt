package edu.emailman.weeklymenu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.emailman.weeklymenu.data.model.Category
import edu.emailman.weeklymenu.ui.theme.BeefColor
import edu.emailman.weeklymenu.ui.theme.ChickenColor
import edu.emailman.weeklymenu.ui.theme.EatOutColor
import edu.emailman.weeklymenu.ui.theme.FishColor
import edu.emailman.weeklymenu.ui.theme.LeftoversColor
import edu.emailman.weeklymenu.ui.theme.PorkColor
import edu.emailman.weeklymenu.ui.theme.VegetarianColor
import edu.emailman.weeklymenu.ui.theme.WildcardColor

@Composable
fun CategoryChip(
    category: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor = getCategoryColor(category)
    val textColor = if (backgroundColor == ChickenColor || backgroundColor == PorkColor ||
                        backgroundColor == LeftoversColor || backgroundColor == EatOutColor) {
        Color.Black
    } else {
        Color.White
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = Category.fromString(category).displayName,
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
    }
}

fun getCategoryColor(category: String): Color {
    return when (Category.fromString(category)) {
        Category.PORK -> PorkColor
        Category.FISH -> FishColor
        Category.VEGETARIAN -> VegetarianColor
        Category.BEEF -> BeefColor
        Category.CHICKEN -> ChickenColor
        Category.WILDCARD -> WildcardColor
        Category.LEFTOVERS -> LeftoversColor
        Category.EAT_OUT -> EatOutColor
    }
}
