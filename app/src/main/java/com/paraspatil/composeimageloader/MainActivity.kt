package com.paraspatil.composeimageloader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
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

    val items = remember {
        listOf(
            DemoImage(
                "Vibrant Mountain",
                "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=1080&q=80"
            ),
            DemoImage(
                "Skeleton Loading State",
                "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=4000&q=100"
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
                "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=2000", // High res
                "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=50"   // Tiny thumb
            ),
            DemoImage(
                "Animated GIF (Resource)",
                url = null,
                resId = R.drawable.test00
            ),
            DemoImage(
                "Animated WebP Support",
                "https://www.gstatic.com/webp/animated/1.webp"
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("AsyncPic v2.3 Demo") }
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
                                selectedImage = item
                            },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Box {
                            val source = when {
                                item.resId != null -> ImageSource.Resources(item.resId)
                                item.thumbnailUrl != null -> ImageSource.Progressive(item.url ?: "", item.thumbnailUrl)
                                item.url != null -> ImageSource.Url(item.url)
                                else -> ImageSource.Url("")
                            }
                            AsyncPic(
                                source = source,
                                placeholderUrl = item.thumbnailUrl,
                                placeholderType = if (item.title == "Skeleton Loading State") ImageSource.PlaceholderType.SKELETON else ImageSource.PlaceholderType.SHIMMER,
                                shimmerColor = if (item.title == "Skeleton Loading State") Color(0xFF334155) else Color(0xFFCBD5E1),
                                blurRadius = if (index == 4) 15 else 0,
                                modifier = Modifier.fillMaxSize(),
                                // minShimmerTime controls when high-res replaces thumbnail
                                minShimmerTime = when (item.title) {
                                    "Skeleton Loading State" -> 5000L // 5 seconds of pulsing
                                    "Loading / Shimmer State" -> 8000L // 8 seconds of shimmering
                                    "Progressive Loading" -> 5000L     // 5 seconds of blurred thumbnail
                                    "Vibrant Mountain", "Tropical Paradise" -> 2000L // Standard delay
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
                        .background(Color.Black)
                ) {
                    val source = when {
                        image.resId != null -> ImageSource.Resources(image.resId)
                        else -> ImageSource.Url(image.url ?: "")
                    }
                    AsyncPic(
                        source = source,
                        modifier = Modifier.fillMaxSize(),
                        zoomable = true,
                        contentScale = ContentScale.Fit
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
