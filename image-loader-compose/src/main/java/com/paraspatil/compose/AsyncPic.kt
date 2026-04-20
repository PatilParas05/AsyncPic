package com.paraspatil.compose

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import kotlinx.coroutines.delay

//AsyncPic V2.1 - Fixed GIF/WebP Support
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

    // CREATE CUSTOM IMAGE LOADER WITH GIF/WEBP SUPPORT
    val gifImageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

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

    // Check if URL is animated format
    val isAnimated = remember(source) {
        val url = when (source) {
            is ImageSource.Url -> source.value
            is ImageSource.Progressive -> source.finalUrl
            else -> ""
        }
        url.endsWith(".gif", ignoreCase = true) ||
                url.endsWith(".webp", ignoreCase = true)
    }

    // controls shimmer/blur duration - skip for animated
    var allowClearImage by remember(source) { mutableStateOf(false) }

    LaunchedEffect(source) {
        if (isAnimated) {
            // Show animated content immediately
            allowClearImage = true
        } else {
            // Apply delay only for static images
            allowClearImage = false
            delay(minShimmerTime)
            allowClearImage = true
        }
    }

    Box(modifier = modifier
        .then(if (!zoomable) Modifier.clip(shape) else Modifier)
        .then(if (zoomable) Modifier.zoomable() else Modifier)
    ) {
        // Thumbnail/Blurred layer (only for Progressive)
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

        // High-res layer with GIF-enabled loader
        SubcomposeAsyncImage(
            model = imageRequest,
            imageLoader = gifImageLoader,  // USE THE GIF-ENABLED LOADER
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

            success = {
                // ALWAYS show animated images immediately
                if (isAnimated || allowClearImage) {
                    SubcomposeAsyncImageContent()
                } else {
                    // Show placeholder/blur for static images during delay
                    if (source is ImageSource.Progressive && source.thumbnailUrl != null) {
                        // Let thumbnail underneath show
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

            error = {
                Box(Modifier.fillMaxSize()) {
                    error()
                }
            }
        )
    }
}