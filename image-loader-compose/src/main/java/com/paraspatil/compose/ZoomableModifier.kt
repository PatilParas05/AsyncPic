package com.paraspatil.compose

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.zoomable(): Modifier = composed {

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var lastScale by remember { mutableStateOf(1f) }

    pointerInput(Unit) {
        detectTransformGestures { _, pan, zoom, _ ->
            scale = (lastScale * zoom).coerceIn(1f, 5f)
            offset += pan
        }
    }.pointerInput(Unit) {
        detectTapGestures(
            onDoubleTap = {
                if (scale > 1f) {
                    scale = 1f
                    offset = Offset.Zero
                } else {
                    scale = 3f
                }
                lastScale = scale
            }
        )
    }.graphicsLayer(
        scaleX = scale,
        scaleY = scale,
        translationX = offset.x,
        translationY = offset.y
    )
}
