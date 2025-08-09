package com.assignment.zospend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.assignment.zospend.ui.navigation.AppNavHost
import com.assignment.zospend.ui.theme.ZospendTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZospendTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Zospend") }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AppNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ZospendTheme {
        val navController = rememberNavController()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Zospend") }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}