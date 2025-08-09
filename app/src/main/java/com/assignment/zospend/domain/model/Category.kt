package com.assignment.zospend.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.ui.graphics.vector.ImageVector

enum class Category(val icon: ImageVector) {
    STAFF(Icons.Default.Group),
    TRAVEL(Icons.Default.CardTravel),
    FOOD(Icons.Default.Fastfood),
    UTILITY(Icons.Default.Receipt)
}
