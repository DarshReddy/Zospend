package com.assignment.zospend.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ScreenWrapper(
    isLoading: Boolean,
    isEmpty: Boolean = false,
    emptyContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            isEmpty && emptyContent != null -> {
                emptyContent()
            }

            else -> {
                content()
            }
        }
    }
}