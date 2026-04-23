package com.paraspatil.compose

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DefaultShimmer(
    color: Color = Color(0xFFE2E8F0),
    direction: ImageSource.ShimmerDirection = ImageSource.ShimmerDirection.DIAGONAL
) {
    val shimmerColors = listOf(
        color.copy(alpha = 0.9f),
        color.copy(alpha = 0.2f),
        color.copy(alpha = 0.9f),
    )

    val transition = rememberInfiniteTransition(label = "shimmer_transition")

    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1500f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1300,
                easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1f)
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )

    val brush = when (direction) {
        ImageSource.ShimmerDirection.DIAGONAL -> Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim - 700f, translateAnim - 700f),
            end = Offset(translateAnim, translateAnim)
        )
        ImageSource.ShimmerDirection.LTR -> Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim - 700f, 0f),
            end = Offset(translateAnim, 0f)
        )
        ImageSource.ShimmerDirection.RTL -> Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(1000f - translateAnim + 700f, 0f),
            end = Offset(1000f - translateAnim, 0f)
        )
        ImageSource.ShimmerDirection.TTB -> Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(0f, translateAnim - 700f),
            end = Offset(0f, translateAnim)
        )
        ImageSource.ShimmerDirection.BTT -> Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(0f, 1500f - translateAnim + 700f),
            end = Offset(0f, 1500f - translateAnim)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush)
    )
}

@Composable
fun DefaultError() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F5F9)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Error",
            tint = Color(0xFF94A3B8).copy(alpha = 0.6f),
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
fun SkeletonPlaceholder(color: Color = Color(0xFFE2E8F0)) {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val alpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse"
    )
    Box(
        Modifier
            .fillMaxSize()
            .background(color.copy(alpha = alpha))
    )
}
