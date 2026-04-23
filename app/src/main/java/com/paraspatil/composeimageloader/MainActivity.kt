package com.paraspatil.composeimageloader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.request.CachePolicy
import com.paraspatil.compose.AsyncPic
import com.paraspatil.compose.ImageSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DemoTheme {
                AsyncPicDemoScreen()
            }
        }
    }
}

data class DemoImage(
    val title: String,
    val url: String?,
    val thumbnailUrl: String? = null,
    val resId: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsyncPicDemoScreen() {
    var selectedImage by remember { mutableStateOf<DemoImage?>(null) }
    var adaptiveColor by remember { mutableStateOf(Color(0xFF1E293B))}
    val animatedColor by animateColorAsState(
        targetValue = adaptiveColor,
        label = "color_animation",
        animationSpec = tween(durationMillis = 800)
    )

    val items = remember {
        listOf(
            DemoImage(
                "Vibrant Mountain",
                "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=1080&q=80"
            ),
            DemoImage(
                "Skeleton Loading State",
                "https://images.unsplash.com/photo-1776410866978-171cc3033431?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxmZWF0dXJlZC1waG90b3MtZmVlZHwxNDF8fHxlbnwwfHx8fHw%3D"
            ),
            DemoImage(
                "Tropical Paradise",
                "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=1080&q=80"
            ),
            DemoImage(
                "Loading / Shimmer State",
                "https://httpstat.us/200?sleep=8000"
            ),
            DemoImage(
                "Error / Failure State",
                "https://invalid.url/image.png"
            ),
            DemoImage(
                "Progressive Loading",
                "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=2000",
                "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=50"
            ),
            DemoImage(
                "Animated GIF (Resource)",
                "https://media.tenor.com/b0ZXAm867pYAAAAM/jujutsu-kaisen-season-3.gif"
            ),
            DemoImage(
                "Animated WebP Support",
                "https://colinbendell.github.io/webperf/animated-gif-decode/2.webp"
            ),
            DemoImage(
                "SVG Support (Vector)",
                "https://raw.githubusercontent.com/coil-kt/coil/main/logo.svg"
            ),
            DemoImage(
                "Circle Crop (Profile Style)",
                "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=500"
            ),
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("AsyncPic v2.6.0 Demo") }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(items) { index, item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clickable(enabled = item.url != null || item.resId != null) {
                                adaptiveColor = Color(0xFF1E293B)
                                selectedImage = item
                            },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Box {
                            val source = when {
                                item.resId != null -> ImageSource.Resources(item.resId)
                                item.thumbnailUrl != null -> ImageSource.Progressive(
                                    item.url ?: "",
                                    item.thumbnailUrl
                                )

                                item.url != null -> ImageSource.Url(item.url)
                                else -> ImageSource.Url("")
                            }
                            AsyncPic(
                                source = source,
                                circleCrop = item.title == "Circle Crop (Profile Style)",
                                diskCachePolicy = if (item.title == "No Cache Demo") CachePolicy.DISABLED else CachePolicy.ENABLED,
                                placeholderUrl = item.thumbnailUrl,
                                placeholderType = if (item.title == "Skeleton Loading State") ImageSource.PlaceholderType.SKELETON else ImageSource.PlaceholderType.SHIMMER,
                                shimmerColor = if (item.title == "Skeleton Loading State") Color(
                                    0xFF334155
                                ) else Color(0xFFCBD5E1),
                                blurRadius = if (index == 4) 15 else 0,
                                modifier = Modifier.fillMaxSize(),
                                onPaletteLoaded = { palette ->
                                    if (item.title == "Adaptive Color Demo") {
                                        val targetColor = palette.vibrant  ?: palette.lightVibrant ?: palette.darkVibrant ?: palette.dominant ?: palette.muted
                                        targetColor?.let { adaptiveColor = it }
                                    }
                                },
                                minShimmerTime = when (item.title) {
                                    "Skeleton Loading State" -> 5000L
                                    "Loading / Shimmer State" -> 8000L
                                    "Progressive Loading" -> 5000L
                                    "Vibrant Mountain", "Tropical Paradise" -> 2000L
                                    else -> 0L
                                }
                            )

                            Surface(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .fillMaxWidth(),
                                color = Color.Black.copy(alpha = 0.6f)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = item.title,
                                        color = Color.White,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    val adaptiveItem = remember {
                        DemoImage(
                            "Adaptive Color Demo",
                            "https://images.unsplash.com/photo-1775027814967-cac2cf014ab7?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxmZWF0dXJlZC1waG90b3MtZmVlZHw0M3x8fGVufDB8fHx8fA%3D%3D"
                        )
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clickable {
                                selectedImage = adaptiveItem
                            },
                        colors = CardDefaults.cardColors(containerColor = animatedColor),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Box {
                            AsyncPic(
                                source = ImageSource.Url(adaptiveItem.url!!),
                                onPaletteLoaded = { palette ->
                                    val targetColor = palette.vibrant
                                        ?: palette.lightVibrant
                                        ?:palette.dominant
                                        ?:palette.muted
                                        ?:palette.darkVibrant

                                    targetColor?.let { adaptiveColor = it }
                                },
                                modifier = Modifier.fillMaxSize()
                            )

                            Surface(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .fillMaxWidth(),
                                color = Color.Black.copy(alpha = 0.6f)
                            ) {
                                Text(
                                    "Adaptive Color Demo",
                                    modifier = Modifier.padding(12.dp),
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = selectedImage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            selectedImage?.let { image ->
                BackHandler { selectedImage = null }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(animatedColor) // Use adaptive background
                ) {
                    val source = when {
                        image.resId != null -> ImageSource.Resources(image.resId)
                        else -> ImageSource.Url(image.url ?: "")
                    }
                    AsyncPic(
                        source = source,
                        modifier = Modifier.fillMaxSize(),
                        zoomable = true,
                        contentScale = ContentScale.Fit,
                        onPaletteLoaded = { palette ->
                            val targetColor = palette.vibrant
                                ?: palette.lightVibrant
                                ?: palette.darkVibrant
                                ?: palette.dominant
                                ?: palette.muted
                            targetColor?.let { adaptiveColor = it }
                        }
                    )

                    IconButton(
                        onClick = { selectedImage = null },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier
                                .background(
                                    Color.Black.copy(alpha = 0.5f),
                                    CircleShape
                                )
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DemoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Color(0xFF0F172A),
            surface = Color(0xFF1E293B),
            onSurface = Color.White
        ),
        content = content
    )
}
@Preview(showBackground = true, name = "AsyncPic Default Preview")
@Composable
fun AsyncPicPreview() {
    DemoTheme {
        Box(modifier = Modifier.size(300.dp).padding(16.dp)) {
            AsyncPic(
                source = ImageSource.Url("https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=1080&q=80"),
                placeholderType = ImageSource.PlaceholderType.SKELETON,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "AsyncPic Circle Preview")
@Composable
fun AsyncPicCirclePreview() {
    DemoTheme {
        Box(modifier = Modifier.size(150.dp).padding(16.dp)) {
            AsyncPic(
                source = ImageSource.Url("https://any-url.com/profile.jpg"),
                circleCrop = true
            )
        }
    }
}