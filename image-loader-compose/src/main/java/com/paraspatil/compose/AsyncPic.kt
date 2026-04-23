package com.paraspatil.compose

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import kotlinx.coroutines.delay

//AsyncPic V2.7.0 - Directional Shimmer
@Composable
fun AsyncPic(
    source: ImageSource,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    shape: Shape = RectangleShape,
    placeholderUrl: String? = null,
    blurRadius: Int = 0,
    circleCrop: Boolean = false,
    placeholder: @Composable () -> Unit = { DefaultShimmer() },
    placeholderType: ImageSource.PlaceholderType = ImageSource.PlaceholderType.SHIMMER,//Default to shimmer
    shimmerColor: Color = Color(0xFFF1F5F9),
    shimmerDirection: ImageSource.ShimmerDirection = ImageSource.ShimmerDirection.DIAGONAL,
    error: @Composable () -> Unit = { DefaultError() },
    zoomable: Boolean = false,
    minShimmerTime: Long = 1000,
    contentScale: ContentScale = ContentScale.Crop,
    diskCachePolicy: CachePolicy = CachePolicy.ENABLED,
    memoryCachePolicy: CachePolicy = CachePolicy.ENABLED,
    onPaletteLoaded:((AsyncPicPalette)->Unit)?=null,
    onSuccess: (() -> Unit)? = null,
    onError: ((Throwable) -> Unit)? = null,
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    // Create custom image Loader With GIF/WEBP Support
    val gifImageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())//Support for .svg files
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .okHttpClient {
                okhttp3.OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                            .header("Accept", "image/svg+xml,image/*,*/*")
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            }
            .build()
    }

    val imageRequest = remember(source, placeholderUrl, blurRadius,diskCachePolicy,memoryCachePolicy,circleCrop) {
        val data = when (source) {
            is ImageSource.Url -> source.value
            is ImageSource.Resources -> source.resId
            is ImageSource.Progressive -> source.finalUrl
        }

        ImageRequest.Builder(context)
            .data(data)
            .placeholderMemoryCacheKey(placeholderUrl)
            .crossfade(true)
            .diskCachePolicy(diskCachePolicy)
            .memoryCachePolicy(memoryCachePolicy)
            .apply {
                val isSvg = when {
                    data is String && data.contains(".svg", ignoreCase = true) -> true
                    else -> false
                }

                if (isSvg) {
                    decoderFactory(SvgDecoder.Factory())
                    size(coil.size.Size.ORIGINAL)
                }

                if (circleCrop) {
                    transformations(CircleCropTransformation())
                }
            }
            .build()
    }

    // Check if URL is animated format
    val isAnimated = remember(source) {
        when (source) {
            is ImageSource.Url -> {
                source.value.endsWith(".gif", true) || source.value.endsWith(".webp", true) || source.value.endsWith(".svg", true)
            }
            is ImageSource.Progressive -> {
                source.finalUrl.endsWith(".gif", true) || source.finalUrl.endsWith(".webp", true) || source.finalUrl.endsWith(".svg", true)
            }
            is ImageSource.Resources -> {
                // Assume resources could be animated if they are from raw or specific drawable types
                // For simplicity, we could return true or implement more complex logic
                true
            }
        }
    }

    val currentPlaceholder = @Composable {
        when (placeholderType) {
            ImageSource.PlaceholderType.SHIMMER -> DefaultShimmer(
                color = shimmerColor,
                direction = shimmerDirection
            )
            ImageSource.PlaceholderType.SKELETON -> SkeletonPlaceholder(color = shimmerColor)
            ImageSource.PlaceholderType.NONE -> {}
        }
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

    Box(
        modifier = modifier
            .then(if (!zoomable) Modifier.clip(shape) else Modifier)
            .then(if (zoomable) Modifier.zoomable() else Modifier)
    ) {
        if (isPreview) {
            /**
             * CORRECTED PREVIEW LOGIC:
             * We use a standard Image component because AsyncImage/Coil
             * often shows a blank box in the Android Studio Design Tab.
             */
            Image(
                painter = painterResource(id = R.drawable.asyncpic), // Use your local test resource
                contentDescription = "Preview Mode",
                contentScale = contentScale,
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (circleCrop) Modifier.clip(androidx.compose.foundation.shape.CircleShape)
                        else Modifier
                    )
            )
        } else {
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
                imageLoader = gifImageLoader,  // Use The GIF-enabled Loader
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize(),
                onSuccess = { state ->
                    onSuccess?.invoke()
                    if (onPaletteLoaded != null) {
                        val drawable = state.result.drawable
                        // generate() runs on a background thread automatically
                        Palette.from(drawable.toBitmap()).generate { palette ->
                            palette?.let {
                                val data = AsyncPicPalette(
                                    vibrant = it.vibrantSwatch?.rgb?.let { Color(it) },
                                    dominant = it.dominantSwatch?.rgb?.let { Color(it) },
                                    muted = it.mutedSwatch?.rgb?.let { Color(it) },
                                    lightVibrant = it.lightVibrantSwatch?.rgb?.let { Color(it) },
                                    darkVibrant = it.darkVibrantSwatch?.rgb?.let { Color(it) },
                                )
                                onPaletteLoaded.invoke(data)
                            }
                        }
                    }
                },
                onError = { state ->
                    onError?.invoke(state.result.throwable)
                },
                loading = {
                    Box(Modifier.fillMaxSize()) {
                        currentPlaceholder()
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
                                imageLoader = gifImageLoader,
                                contentDescription = null,
                                contentScale = contentScale,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(Modifier.fillMaxSize()) {
                                currentPlaceholder()
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
}
