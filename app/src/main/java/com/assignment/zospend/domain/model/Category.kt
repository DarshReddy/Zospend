package com.assignment.zospend.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class Category(val icon: ImageVector, val color: Color) {
    STAFF(Icons.Default.Group, Color.Blue),
    TRAVEL(Icons.Default.CardTravel, Color.Magenta),
    FOOD(Icons.Default.Fastfood, Color.Red),
    UTILITY(Icons.Default.Receipt, Color.DarkGray)
}
