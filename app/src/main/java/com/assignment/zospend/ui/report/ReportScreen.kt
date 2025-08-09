package com.assignment.zospend.ui.report

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.assignment.zospend.ui.theme.ZospendTheme

@Composable
fun ReportScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Report Screen", modifier = Modifier.align(Alignment.Center))
    }
}

@Preview(showBackground = true)
@Composable
fun ReportScreenPreview() {
    ZospendTheme {
        ReportScreen()
    }
}
