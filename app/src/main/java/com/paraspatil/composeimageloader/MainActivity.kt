package com.paraspatil.composeimageloader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.paraspatil.compose.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeImageLoaderTheme {
                LibraryShowcaseApp()
            }
        }
    }
}

data class ShowcaseImage(
    val id: Int,
    val title: String,
    val feature: String,
    val imageUrl: String,
    val transformation: ImageTransformation,
    var likes: Int = 0,
    var isLiked: Boolean = false
)

data class LibraryFeature(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val description: String,
    val gradient: List<Color>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun LibraryShowcaseApp() {
    var images by remember {
        mutableStateOf(
            listOf(
                ShowcaseImage(1, "Mountain Peak", "RoundedCorners 16dp",
                    "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop",
                    ImageTransformation.RoundedCorners(16.dp), 234),
                ShowcaseImage(2, "Ocean Sunset", "Circle Transform",
                    "https://images.unsplash.com/photo-1505142468610-359e7d316be0?w=800&h=600&fit=crop",
                    ImageTransformation.Circle, 189),
                ShowcaseImage(3, "City Skyline", "RoundedCorners 25dp",
                    "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=800&h=600&fit=crop",
                    ImageTransformation.RoundedCorners(25.dp), 456),
                ShowcaseImage(4, "Forest Path", "Error Handling",
                    "https://invalid-url-to-show-error.com/image.jpg",
                    ImageTransformation.RoundedCorners(16.dp), 312),
                ShowcaseImage(5, "Desert Landscape", "RoundedCorners 30dp",
                    "https://images.unsplash.com/photo-1509316785289-025f5b846b35?w=800&h=600&fit=crop",
                    ImageTransformation.RoundedCorners(30.dp), 278),
                ShowcaseImage(6, "Profile Avatar", "Circle Avatar",
                    "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400&h=400&fit=crop",
                    ImageTransformation.Circle, 198),
                ShowcaseImage(7, "Tropical Beach", "Zoomable & ContentScale.Fit",
                    "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&h=600&fit=crop",
                    ImageTransformation.None, 345),
                ShowcaseImage(8, "Night City", "RoundedCorners 40dp",
                    "https://images.unsplash.com/photo-1519501025264-65ba15a82390?w=800&h=600&fit=crop",
                    ImageTransformation.RoundedCorners(40.dp), 221),
                ShowcaseImage(9, "Aurora Sky", "Default Shimmer Loading",
                    "https://images.unsplash.com/photo-1531366936337-7c912a4589a7?w=800&h=600&fit=crop",
                    ImageTransformation.RoundedCorners(12.dp), 567),
                ShowcaseImage(10, "Coffee Cup", "Circle Profile",
                    "https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=400&h=400&fit=crop",
                    ImageTransformation.Circle, 423),
                ShowcaseImage(11, "Mountains Sunset", "Custom Placeholder",
                    "https://images.unsplash.com/photo-1540979388789-6cee28a1cdc9?w=800&h=600&fit=crop",
                    ImageTransformation.RoundedCorners(16.dp), 689),
                ShowcaseImage(12, "Tech Workspace", "ContentScale Crop",
                    "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=800&h=600&fit=crop",
                    ImageTransformation.RoundedCorners(14.dp), 534),
            )
        )
    }

    var searchQuery by remember { mutableStateOf("") }
    var showInfo by remember { mutableStateOf(false) }
    var showFeatures by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf<ShowcaseImage?>(null) }

    val filteredImages = images.filter { image ->
        image.title.contains(searchQuery, ignoreCase = true) ||
                image.feature.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            ShowcaseTopBar(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                onInfoClick = { showInfo = true },
                onFeaturesClick = { showFeatures = true }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0f172a),
                            Color(0xFF1e1b4b),
                            Color(0xFF312e81)
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            if (filteredImages.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredImages) { image ->
                        ShowcaseCard(
                            image = image,
                            onClick = { selectedImage = it },
                            onLikeClick = { id ->
                                images = images.map {
                                    if (it.id == id) {
                                        it.copy(
                                            isLiked = !it.isLiked,
                                            likes = if (it.isLiked) it.likes - 1 else it.likes + 1
                                        )
                                    } else it
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showInfo) {
        LibraryInfoDialog(onDismiss = { showInfo = false })
    }

    if (showFeatures) {
        FeaturesDialog(onDismiss = { showFeatures = false })
    }

    selectedImage?.let { image ->
        FullscreenImageViewer(
            image = image,
            onDismiss = { selectedImage = null },
            onLikeClick = {
                images = images.map {
                    if (it.id == image.id) {
                        it.copy(
                            isLiked = !it.isLiked,
                            likes = if (it.isLiked) it.likes - 1 else it.likes + 1
                        )
                    } else it
                }
                selectedImage = images.find { it.id == image.id }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowcaseTopBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onInfoClick: () -> Unit,
    onFeaturesClick: () -> Unit
) {
    Surface(
        color = Color(0xFF0f0f1e).copy(alpha = 0.95f),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ComposeImageLoader",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF8b5cf6), Color(0xFFec4899))
                            )
                        )
                    )
                    Text(
                        text = "Library Showcase Demo",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(onClick = onFeaturesClick) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Features",
                            tint = Color(0xFFfbbf24)
                        )
                    }
                    IconButton(onClick = onInfoClick) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Library Info",
                            tint = Color(0xFF8b5cf6)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                placeholder = { Text("Search features or images...", color = Color.White.copy(alpha = 0.5f)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White.copy(alpha = 0.5f)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.1f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color(0xFF8b5cf6),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
        }
    }
}

@Composable
fun ShowcaseCard(
    image: ShowcaseImage,
    onClick: (ShowcaseImage) -> Unit,
    onLikeClick: (Int) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    // Determine which placeholder to use
    val placeholderComposable: @Composable () -> Unit = when {
        image.feature.contains("Custom Placeholder", ignoreCase = true) -> {
            { CustomPlaceholder() }
        }
        else -> {
            { DefaultShimmer() }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = {
                        onClick(image)
                    }
                )
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImageLoader(
                data = ImageRequestData(
                    url = image.imageUrl,
                    contentDescription = image.title,
                    transform = image.transformation,
                    placeholder = placeholderComposable,
                    error = { CustomErrorDisplay(image.title) }
                ),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 300f
                        )
                    )
            )

            Surface(
                color = Color(0xFF8b5cf6).copy(alpha = 0.9f),
                shape = RoundedCornerShape(bottomEnd = 20.dp),
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Text(
                    text = image.feature,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = image.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                LikeButton(
                    isLiked = image.isLiked,
                    likes = image.likes,
                    onClick = { onLikeClick(image.id) }
                )
            }
        }
    }
}


@Composable
fun CustomErrorDisplay(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a1a2e)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.BrokenImage,
                contentDescription = "Error",
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Failed to load",
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = title,
                color = Color.White.copy(alpha = 0.5f),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun CustomPlaceholder() {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF8b5cf6).copy(alpha = alpha * 0.3f),
                        Color(0xFFec4899).copy(alpha = alpha * 0.2f),
                        Color(0xFF06b6d4).copy(alpha = alpha * 0.1f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.ImageSearch,
            contentDescription = null,
            tint = Color.White.copy(alpha = alpha * 0.6f),
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer {
                    rotationZ = rotation
                }
        )
    }
}

@Composable
fun LikeButton(
    isLiked: Boolean,
    likes: Int,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isLiked) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Like",
            tint = if (isLiked) Color(0xFFef4444) else Color.White.copy(alpha = 0.7f),
            modifier = Modifier.scale(scale)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = likes.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.8f),
            fontWeight = FontWeight.Medium
        )
    }
}
@Composable
fun FullscreenImageViewer(
    image: ShowcaseImage,
    onDismiss: () -> Unit,
    onLikeClick: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AsyncImageLoader(
                data = ImageRequestData(
                    url = image.imageUrl,
                    contentDescription = image.title,
                    transform = ImageTransformation.None,
                    placeholder = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF8b5cf6))
                        }
                    },
                    error = { CustomErrorDisplay(image.title) }
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .zoomable(),
                contentScale = ContentScale.Fit
            )

            Surface(
                color = Color.Black.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = image.title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                        Text(
                            text = image.feature,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF8b5cf6)
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }
            }

            Surface(
                color = Color.Black.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 72.dp)
                ) {
                    Surface(
                        color = Color(0xFF8b5cf6).copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ZoomIn,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Pinch to zoom • Double tap • Drag to pan",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(onClick = onLikeClick) {
                            Icon(
                                imageVector = if (image.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (image.isLiked) Color(0xFFef4444) else Color.White
                            )
                        }

                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.White
                            )
                        }

                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Download",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturesDialog(onDismiss: () -> Unit) {
    val features = remember {
        listOf(
            LibraryFeature(
                icon = Icons.Default.Api,
                title = "Unified API",
                description = "Single composable for all image loading needs",
                gradient = listOf(Color(0xFF8b5cf6), Color(0xFFa855f7))
            ),
            LibraryFeature(
                icon = Icons.Default.Brush,
                title = "Built-in Transformations",
                description = "Circle, rounded corners, and custom shapes",
                gradient = listOf(Color(0xFFec4899), Color(0xFFf472b6))
            ),
            LibraryFeature(
                icon = Icons.Default.FlashOn,
                title = "Smart Loading States",
                description = "Automatic placeholder, loading, and error handling",
                gradient = listOf(Color(0xFF06b6d4), Color(0xFF22d3ee))
            ),
            LibraryFeature(
                icon = Icons.Default.Image,
                title = "Multiple Sources",
                description = "Load from URLs, files, or resources",
                gradient = listOf(Color(0xFF10b981), Color(0xFF34d399))
            ),
            LibraryFeature(
                icon = Icons.Default.TouchApp,
                title = "Gesture Support",
                description = "Built-in pinch-to-zoom and pan capabilities",
                gradient = listOf(Color(0xFFf59e0b), Color(0xFFfbbf24))
            ),
            LibraryFeature(
                icon = Icons.Default.Palette,
                title = "Customizable UI",
                description = "Custom placeholders and error composables",
                gradient = listOf(Color(0xFFef4444), Color(0xFFf87171))
            ),
            LibraryFeature(
                icon = Icons.Default.RocketLaunch,
                title = "Powered by Coil",
                description = "Leverages Coil's efficient image loading",
                gradient = listOf(Color(0xFF6366f1), Color(0xFF818cf8))
            )
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0f0f1e))
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "✨ Features",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "What makes us special",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Features List
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(features) { feature ->
                        FeatureCard(feature)
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureCard(feature: LibraryFeature) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with gradient background
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(feature.gradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Text content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = feature.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = feature.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun LibraryInfoDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1a1a2e))
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Library Info",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "ComposeImageLoader",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF8b5cf6), Color(0xFFec4899))
                        )
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "A powerful and flexible image loading library for Jetpack Compose",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8b5cf6)
                    )
                ) {
                    Text("Got it!")
                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = "No Results",
                modifier = Modifier.size(64.dp),
                tint = Color.White.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No images found",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Try searching for a different feature",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun ComposeImageLoaderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF8b5cf6),
            secondary = Color(0xFFec4899),
            background = Color(0xFF0f172a),
            surface = Color(0xFF1a1a2e)
        ),
        content = content
    )
}