package com.assignment.zospend

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.assignment.zospend.ui.main.MainScreen
import com.assignment.zospend.ui.theme.ThemeViewModel
import com.assignment.zospend.ui.theme.ZospendTheme

class MainActivity : ComponentActivity() {

    private val themeViewModel by viewModels<ThemeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                ) { isDarkTheme },
                navigationBarStyle = SystemBarStyle.auto(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                ) { isDarkTheme },
            )
            ZospendTheme(
                darkTheme = isDarkTheme
            ) {
                MainScreen(
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { themeViewModel.toggleTheme() }
                )
            }
        }
    }
}