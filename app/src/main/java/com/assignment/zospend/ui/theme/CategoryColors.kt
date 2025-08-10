package com.assignment.zospend.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.assignment.zospend.domain.model.Category

@Composable
fun categoryColor(category: Category): Color {
    return when (category) {
        Category.STAFF -> if (isSystemInDarkTheme()) C1_D else C1_L
        Category.TRAVEL -> if (isSystemInDarkTheme()) C2_D else C2_L
        Category.FOOD -> if (isSystemInDarkTheme()) C3_D else C3_L
        Category.UTILITY -> if (isSystemInDarkTheme()) C4_D else C4_L
    }
}