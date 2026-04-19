package com.paraspatil.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import kotlinx.coroutines.delay

//AsyncPic V2.1
@Composable
fun AsyncPic(
    source: ImageSource,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    shape: Shape = RectangleShape,
    placeholderUrl: String? = null,
    blurRadius: Int = 0,
    placeholder: @Composable () -> Unit = { DefaultShimmer() },
    error: @Composable () -> Unit = { DefaultError() },
    zoomable: Boolean = false,
    minShimmerTime: Long = 1000,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current

    val imageRequest = remember(source, placeholderUrl, blurRadius) {
        val data = when (source) {
            is ImageSource.Url -> source.value
            is ImageSource.Resources -> source.resId
            is ImageSource.Progressive -> source.finalUrl
        }

        ImageRequest.Builder(context)
            .data(data)
            .placeholderMemoryCacheKey(placeholderUrl)
            .crossfade(true)
            .build()
    }

    // controls shimmer/blur duration
    var allowClearImage by remember(source) { mutableStateOf(false) }

    LaunchedEffect(source) {
        allowClearImage = false
        delay(minShimmerTime)
        allowClearImage = true
    }

    Box(modifier = modifier
        .then(if (!zoomable) Modifier.clip(shape) else Modifier)
        .then(if (zoomable) Modifier.zoomable() else Modifier)
    ) {
        // Thumbnail/Blurred layer
        if (source is ImageSource.Progressive && source.thumbnailUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(source.thumbnailUrl)
                    .crossfade(true)
                    .apply {
                        if (blurRadius > 0) {
                            transformations(CustomBlurTransformation(context, blurRadius.toFloat()))
                        }
                    }
                    .build(),
                contentDescription = null,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
        }

        // High-res layer
        SubcomposeAsyncImage(
            model = imageRequest,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize(),

            loading = {
                if (source !is ImageSource.Progressive || source.thumbnailUrl == null) {
                    Box(Modifier.fillMaxSize()) {
                        placeholder()
                    }
                }
            },

            // success wait for blur time
            success = {
                if (allowClearImage) {
                    SubcomposeAsyncImageContent()
                } else {
                    // While waiting, show blurred high-res if it's already loaded, 
                    // or just let the thumbnail layer below show.
                    if (source is ImageSource.Progressive && source.thumbnailUrl != null) {
                        // thumbnail is already showing underneath
                    } else if (blurRadius > 0) {
                         AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(imageRequest.data)
                                .transformations(CustomBlurTransformation(context, blurRadius.toFloat()))
                                .build(),
                            contentDescription = null,
                            contentScale = contentScale,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(Modifier.fillMaxSize()) {
                            placeholder()
                        }
                    }
                }
            },

            // error
            error = {
                Box(Modifier.fillMaxSize()) {
                    error()
                }
            }
        )
    }
}
