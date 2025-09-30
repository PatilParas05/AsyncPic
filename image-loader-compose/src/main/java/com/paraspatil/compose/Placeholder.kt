package com.paraspatil.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** A shimmer placeholder while loading */
@Composable
fun DefaultShimmer() { // Removed 'internal'
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    val brush = remember(shimmerColors) {
        Brush.linearGradient(colors = shimmerColors)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Image,
            contentDescription = null,
            tint = Color.DarkGray.copy(alpha = 0.5f)
        )
    }
}

/** A simple error icon placeholder. */
@Composable
fun DefaultErrorIcon() { // Removed 'internal'
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = 0.1f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.BrokenImage,
            contentDescription = "Error loading image",
            tint = Color.DarkGray.copy(alpha = 0.7f),
            modifier = Modifier.size(48.dp)
        )
    }
}
