package com.assignment.zospend.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.assignment.zospend.ui.theme.ZospendTheme

@Composable
fun ExpenseListScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Expense List Screen", modifier = Modifier.align(Alignment.Center))
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseListScreenPreview() {
    ZospendTheme {
        ExpenseListScreen()
    }
}
