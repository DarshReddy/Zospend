package com.assignment.zospend.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.assignment.zospend.R

@Composable
fun SuccessAnimation(onDismiss: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success_anim))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1, // Set to 1 for a single playback
        isPlaying = true // Start playing immediately
    )
    LaunchedEffect(progress) {
        if (progress == 1.0f) {
            onDismiss()
        }
    }
    LottieAnimation(
        composition = composition,
        iterations = 1,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("Success Animation")
    )
}
