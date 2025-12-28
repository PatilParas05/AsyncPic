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
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import coil.size.Size
import kotlinx.coroutines.delay

//AsyncPic V2.1
@Composable
fun AsyncPic(
    url: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    shape: Shape = RectangleShape,
    placeholder: @Composable () -> Unit = { DefaultShimmer() },
    error: @Composable () -> Unit = { DefaultError() },
    zoomable: Boolean = false,
    minShimmerTime: Long = 1000,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current

    // controls shimmer duration
    var allowImage by remember(url) { mutableStateOf(false) }

    LaunchedEffect(url) {
        allowImage = false
        delay(minShimmerTime)
        allowImage = true
    }

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(context)
            .data(url)
            .crossfade(true)
            .size(Size.ORIGINAL)
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
            .then(if (!zoomable) Modifier.clip(shape) else Modifier)
            .then(if (zoomable) Modifier.zoomable() else Modifier),

        // loading always shimmer
        loading = {
            Box(Modifier.fillMaxSize().clip(shape)) {
                placeholder()
            }
        },

        // success wait foe shimmer time
        success = {
            if (allowImage) {
                SubcomposeAsyncImageContent()
            } else {
                Box(Modifier.fillMaxSize().clip(shape)) {
                    placeholder()
                }
            }
        },

        // error
        error = {
            Box(Modifier.fillMaxSize().clip(shape)) {
                error()
            }
        }
    )
}
