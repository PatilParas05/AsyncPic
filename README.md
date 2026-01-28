 <div align="center">
  <img width="500" height="1024" alt="AsyncPicv2" src="https://github.com/user-attachments/assets/8cc6147c-d5ea-4444-ac0a-61d54b383ef5" />
</div>
<div align="center">
 
# 📸 AsyncPic v2.0

**The Modern Image Loading Library for Jetpack Compose**

A powerful, flexible, and developer-friendly image loading solution built on top of Coil, designed specifically for Jetpack Compose applications.

[![](https://jitpack.io/v/PatilParas05/AsyncPic.svg)](https://jitpack.io/#PatilParas05/AsyncPic)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1+-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Compose-1.7+-blue.svg)](https://developer.android.com/jetpack/compose)

[Features](#-features) • [Installation](#-installation) • [Quick Start](#-quick-start) • [Documentation](#-documentation) • [Examples](#-examples) • [Contributing](#-contributing)

</div>

---

## 🎯 What's New in v2.0

- ✅ **Minimum Shimmer Duration** - Control how long the shimmer effect displays
- ✅ **Double-Tap to Zoom** - Smooth zoom in/out with double-tap gesture
- ✅ **Animated Zoom Transitions** - Buttery smooth zoom animations
- ✅ **Improved Error Handling** - Better error states with icons
- ✅ **Enhanced Shimmer Effect** - Diagonal animated shimmer
- ✅ **Better Shape Clipping** - Proper shape handling for all states
- ✅ **Fullscreen Viewer Demo** - Complete example with zoom support

---

## ✨ Features

### Core Features
- 🚀 **Simple API** - Single composable function for all image loading needs
- 🎨 **Shape Support** - Built-in support for rounded corners, circles, and custom shapes
- ⚡ **Smart Loading States** - Automatic shimmer placeholders and error handling
- 🔍 **Zoom & Pan** - Pinch-to-zoom, double-tap zoom, and smooth panning
- 🎭 **Customizable UI** - Custom placeholders and error composables
- 🔄 **Crossfade Animation** - Smooth transitions when images load
- ⏱️ **Controlled Shimmer** - Set minimum shimmer display time for better UX

### Advanced Features
- 📦 **Powered by Coil 2.6** - Efficient caching and loading
- 🎯 **Content Scale Options** - Crop, Fit, FillBounds, and more
- 🖼️ **Multiple Sources** - URLs, files, and drawable resources (planned)
- ♿ **Accessibility** - Full content description support
- 🎬 **Compose-First** - Built for modern Android development

---

## 📦 Installation

### Step 1: Add JitPack Repository

Add JitPack to your **`settings.gradle.kts`** (root level):

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Step 2: Add Dependency

Add AsyncPic to your app module's **`build.gradle.kts`**:

```kotlin
dependencies {
    implementation("com.github.PatilParas05:AsyncPic:v2.0.0")
}
```

### Step 3: Sync Project

Sync your project with Gradle files and you're ready to go! 🎉

---

## 🚀 Quick Start

### Basic Usage

The simplest way to load an image:

```kotlin
AsyncPic(
    url = "https://example.com/image.jpg",
    modifier = Modifier.size(200.dp)
)
```

### With Rounded Corners

```kotlin
AsyncPic(
    url = "https://example.com/profile.jpg",
    modifier = Modifier.size(100.dp),
    shape = RoundedCornerShape(16.dp),
    contentDescription = "Profile Picture"
)
```

### Circular Image

```kotlin
AsyncPic(
    url = "https://example.com/avatar.jpg",
    modifier = Modifier.size(80.dp),
    shape = CircleShape,
    contentDescription = "User Avatar"
)
```

### With Zoom Support

```kotlin
AsyncPic(
    url = "https://example.com/photo.jpg",
    modifier = Modifier.fillMaxSize(),
    zoomable = true,
    contentScale = ContentScale.Fit,
    contentDescription = "Zoomable Photo"
)
```

---

## 📖 Documentation

### AsyncPic Function

```kotlin
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
)
```

### Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `url` | `String?` | **Required** | Image URL to load |
| `modifier` | `Modifier` | `Modifier` | Compose modifier for styling |
| `contentDescription` | `String?` | `null` | Accessibility description |
| `shape` | `Shape` | `RectangleShape` | Clip shape (RoundedCornerShape, CircleShape, etc.) |
| `placeholder` | `@Composable () -> Unit` | `DefaultShimmer()` | Custom loading placeholder |
| `error` | `@Composable () -> Unit` | `DefaultError()` | Custom error state UI |
| `zoomable` | `Boolean` | `false` | Enable pinch-to-zoom and double-tap |
| `minShimmerTime` | `Long` | `1000` | Minimum shimmer display duration (ms) |
| `contentScale` | `ContentScale` | `ContentScale.Crop` | How to scale the image |

### Content Scale Options

- `ContentScale.Crop` - Scale to fill, cropping if necessary (default)
- `ContentScale.Fit` - Scale to fit entirely within bounds
- `ContentScale.FillBounds` - Stretch to fill bounds
- `ContentScale.Inside` - No scaling if image is smaller
- `ContentScale.FillWidth` - Scale to match width
- `ContentScale.FillHeight` - Scale to match height


---

## 🎨 Zoom Gestures

AsyncPic includes powerful zoom functionality:

### Features
- **Pinch to Zoom**: Scale from 1x to 4x
- **Double-Tap**: Toggle between normal and 3x zoom
- **Pan**: Move around when zoomed in
- **Smooth Animations**: 300ms animated transitions
- **Auto-Reset**: Returns to normal when scaled below 1x

### Usage

```kotlin
AsyncPic(
    url = imageUrl,
    modifier = Modifier.fillMaxSize(),
    zoomable = true,
    contentScale = ContentScale.Fit
)
```

> **Note**: When `zoomable = true`, the shape clipping is removed from the image itself to allow zoom to extend beyond bounds. The placeholder and error states still respect the shape.

---

## 🏗️ Architecture

AsyncPic is built on a modern Android architecture:

```
┌─────────────────────────────────────────┐
│         Your Compose UI                 │
│                                         │
│    AsyncPic(url = "...")                │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         AsyncPic Component              │
│  • State Management (Loading/Success)   │
│  • Shape Clipping                       │
│  • Zoom Gesture Handling                │
│  • Minimum Shimmer Duration             │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│    Coil SubcomposeAsyncImage            │
│  • Image Loading                        │
│  • Caching (Memory + Disk)              │
│  • Crossfade Animation                  │
│  • Request Management                   │
└─────────────────────────────────────────┘
```

---

## ⚙️ Requirements

- **Min SDK**: 26 (Android 8.0 Oreo)
- **Target SDK**: 36 (Android 15)
- **Kotlin**: 2.1+
- **Compose**: 1.7+
- **Coil**: 2.6.0

---

## 📱 Sample App

The repository includes a comprehensive demo app showcasing:

- Basic image loading
- Rounded corners and shapes
- Loading states with extended shimmer
- Error handling
- Fullscreen viewer with zoom
- Image grids
- Interactive UI

### Run the Sample

```bash
git clone https://github.com/PatilParas05/AsyncPic.git
cd AsyncPic
./gradlew :app:installDebug
```

---

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

### Ways to Contribute

1. **Report Bugs** - Open an issue with details and reproduction steps
2. **Suggest Features** - Share your ideas for improvements
3. **Submit PRs** - Fix bugs or add features
4. **Improve Docs** - Help make documentation better
5. **Share Feedback** - Let us know how you're using AsyncPic

## 🔄 Migration from v1.x

### Breaking Changes

**v1.x**:
```kotlin
AsyncImageLoader(
    data = ImageRequestData(
        url = imageUrl,
        transform = ImageTransformation.RoundedCorners(16.dp)
    )
)
```

**v2.0**:
```kotlin
AsyncPic(
    url = imageUrl,
    shape = RoundedCornerShape(16.dp)
)
```

### Key Differences

| Feature | v1.x | v2.0 |
|---------|------|------|
| Main function | `AsyncImageLoader` | `AsyncPic` |
| Configuration | `ImageRequestData` class | Direct parameters |
| Transformations | `ImageTransformation` enum | Compose `Shape` |
| Shimmer control | Not available | `minShimmerTime` parameter |
| Double-tap zoom | Not available | Built-in support |

---

## 📊 Performance

AsyncPic is built for performance:

- ✅ **Memory Efficient** - Coil's memory caching
- ✅ **Disk Caching** - Automatic persistent cache
- ✅ **Request Deduplication** - Multiple requests for same URL merged
- ✅ **Bitmap Pooling** - Reuses bitmap memory
- ✅ **Lazy Loading** - Only loads when visible
- ✅ **Cancellation** - Automatic request cancellation

---

## 🐛 Troubleshooting

### Image Not Loading?

1. **Check internet permission** in `AndroidManifest.xml`:
   ```xml
   <uses-permission android:name="android.permission.INTERNET"/>
   ```

2. **Verify URL is accessible**:
   - Open URL in browser
   - Check for HTTPS issues
   - Verify network connection

3. **Check Coil logs** in Logcat:
   ```
   Filter: coil
   ```

### Zoom Not Working?

- Ensure `zoomable = true` is set
- Use `ContentScale.Fit` for best zoom experience
- Check that parent container allows gestures

### Shimmer Shows Too Long?

- Reduce `minShimmerTime` parameter
- Default is 1000ms (1 second)
- Set to `0L` to disable minimum duration

---

## 📝 License

```
MIT License

Copyright (c) 2024 Paras Patil

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 🙏 Acknowledgments

- **[Coil](https://coil-kt.github.io/)** - The amazing image loading library powering AsyncPic
- **[Jetpack Compose](https://developer.android.com/jetpack/compose)** - Modern Android UI toolkit
- **[Unsplash](https://unsplash.com)** - Sample images in demo app
- **[Material Icons](https://fonts.google.com/icons)** - Icons used in examples

---

## ⭐ Show Your Support

If AsyncPic helped you build something awesome, please:

- ⭐ Star this repository
- 🐦 Share on Twitter
- 📝 Write a blog post
- 💬 Spread the word

---
### Community Requests

Have a feature request? [Open an issue](https://github.com/PatilParas05/AsyncPic/issues/new) with the label `enhancement`.

---

<div align="center">
 
 ![Shimmer](https://github.com/user-attachments/assets/68162b2b-ff57-497b-b3e4-46c3bd064118) <br>
 Shimmer
 
</div>

---

<div align="center">
 
![Zoomable](https://github.com/user-attachments/assets/127a0c93-eac6-45c2-b782-4e5f873d1950) <br>
Zoom

</div>

---

<div align="center">

**Made with ❤️ for the Android Compose Community**

[⬆ Back to Top](#-asyncpic-v20)

</div>
