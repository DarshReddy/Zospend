package com.assignment.zospend.ui.entry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.assignment.zospend.ui.theme.ZospendTheme

@Composable
fun EntryScreen(
    onNavigateToList: () -> Unit,
    onNavigateToReport: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Expense Entry Screen", modifier = Modifier.align(Alignment.Center))
        Button(onClick = onNavigateToList, modifier = Modifier.align(Alignment.BottomStart)) {
            Text("Go to List")
        }
        Button(onClick = onNavigateToReport, modifier = Modifier.align(Alignment.BottomEnd)) {
            Text("Go to Report")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EntryScreenPreview() {
    ZospendTheme {
        EntryScreen({}, {})
    }
}
