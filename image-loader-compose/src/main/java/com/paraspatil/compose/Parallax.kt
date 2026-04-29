package com.paraspatil.compose

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

fun Modifier.parallaxLayout(intensity: Float): Modifier = composed {
    if (intensity == 0f) return@composed Modifier
    
    var verticalOffset by remember { mutableStateOf(0f) }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    
    // Get actual screen height in pixels
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    this
        .onGloballyPositioned { coordinates ->
            // Position of the item relative to the root (screen)
            val yPosition = coordinates.positionInRoot().y
            val itemHeight = coordinates.size.height
            
            // Calculate how far the item's center is from the screen center
            val itemCenter = yPosition + itemHeight / 2f
            val screenCenter = screenHeightPx / 2f
            
            // Offset is proportional to the distance from center
            verticalOffset = (itemCenter - screenCenter) * intensity
        }
        .graphicsLayer {
            translationY = verticalOffset
            
            // Scale up slightly to ensure edges are always covered when shifting
            val scale = 1f + (kotlin.math.abs(intensity) * 1.5f)
            scaleX = scale
            scaleY = scale
        }
}
