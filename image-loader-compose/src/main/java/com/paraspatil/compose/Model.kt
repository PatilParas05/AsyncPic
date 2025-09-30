package com.paraspatil.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import java.io.File



/**
 * Defines the type of transformation applied to the final image.
 */
sealed class ImageTransformation {
    data object None : ImageTransformation()
    data object Circle : ImageTransformation()
    data class RoundedCorners(val dp: Dp) : ImageTransformation()
}

/**
 * The unified data model for the image request.
 * Provides flexibility for placeholders, error UI, and retry actions.
 */
data class ImageRequestData(
    // Image Sources (only one should be non-null)
    val url: String? = null,
    val file: File? = null,
    val resId: Int? = null,

    // Accessibility
    val contentDescription: String? = null,

    // Transformation (Circle, Rounded, None)
    val transform: ImageTransformation = ImageTransformation.None,

    // Custom Composable content for states
    val placeholder: @Composable () -> Unit = { DefaultShimmer() },
    val error: @Composable () -> Unit = { DefaultErrorIcon() },

    // Optional retry action (useful for error state with retry button)
    val onRetry: (() -> Unit)? = null
)

/**
 * Helper function to extract the correct data source for Coil.
 */
fun ImageRequestData.dataSource(): Any? {
    return url ?: file ?: resId
}
