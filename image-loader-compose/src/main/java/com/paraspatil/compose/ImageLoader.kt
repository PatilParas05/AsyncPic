package com.paraspatil.compose

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.AsyncImagePainter

/**
 * The highly opinionated, unified Jetpack Compose image loading Composable.
 * Simplifies advanced handling (loading, transformations, animated states).
 */
@Composable
fun AsyncImageLoader(
    data: ImageRequestData,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    // Apply Transformation as a Modifier (for shape clipping)
    val imageModifier = when (val transform = data.transform) {
        is ImageTransformation.RoundedCorners -> modifier.clip(RoundedCornerShape(transform.dp))
        ImageTransformation.Circle -> modifier.clip(CircleShape)
        ImageTransformation.None -> modifier
    }

    // Use SubcomposeAsyncImage to handle loading, error, and success states
    SubcomposeAsyncImage(
        model = data.dataSource(),
        contentDescription = data.contentDescription,
        modifier = imageModifier,
        contentScale = contentScale
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Success -> {
                // Render the actual image
                SubcomposeAsyncImageContent()
            }
            is AsyncImagePainter.State.Loading,
            is AsyncImagePainter.State.Empty -> {
                // Show placeholder/shimmer while loading
                data.placeholder()
            }
            is AsyncImagePainter.State.Error -> {
                // Show error composable if load fails
                data.error()
            }
        }
    }
}
