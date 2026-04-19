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
    val url: String?
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
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("AsyncPic v2.1 Demo") }
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
                            .clickable(enabled = item.url != null) {
                                selectedImage = item
                            },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Box {
                            AsyncPic(
                                url = item.url,
                                modifier = Modifier.fillMaxSize(),
                                shape = RoundedCornerShape(16.dp),
                                contentScale = ContentScale.Crop,

                                // Shimmer ONLY for 2nd & 3rd card
                                minShimmerTime = if (index == 1 || index == 2) 3500L else 0L
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
                    AsyncPic(
                        url = image.url,
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