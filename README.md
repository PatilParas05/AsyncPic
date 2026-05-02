
# 📸 AsyncPic v3.0.0

**The Premium Image Loader for Jetpack Compose**

A high-performance library featuring ultra-smooth shimmers, flicker-free minShimmerTime logic, professional zoom engine (pinch, pan, and double-tap), AGSL shader cinematic reveals, adaptive color morphing, progressive image loading with blur-up effects, GIF/SVG/WebP support, intelligent caching strategies, and color palette extraction for a top-tier user experience.


[![](https://jitpack.io/v/PatilParas05/AsyncPic.svg)](https://jitpack.io/#PatilParas05/AsyncPic)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1+-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Compose-1.7+-blue.svg)](https://developer.android.com/jetpack/compose)

---

## 📦 Installation

### Step 1: Add JitPack Repository

Add JitPack to your **`settings.gradle.kts`** (root level):

```kotlin
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url = uri("https://jitpack.io") }
		}
	}
```

### Step 2: Add Dependency

Add AsyncPic to your app module's **`build.gradle.kts`**:

```kotlin
dependencies {
         implementation("com.github.PatilParas05:AsyncPic:v3.0.0")
	}
```

### Step 3: Sync Project

Sync your project with Gradle files and you're ready to go! 🎉

---
## 📚 Documentation

📖 **[Full Documentation]()** - Comprehensive guides, API reference, and examples

---
## 🎯 What's New in v3.0

- ✨ **AGSL Shader Cinematic Reveals(Android 13+)** - Three stunning shader-based reveal effects (Dissolve, Pixelate, Wipe)
- 🎨 **Adaptive Color Morphing** - Automatic shimmer color morphing to dominant image colors
- 📐 **Palette Extraction** - Extract vibrant, dominant, muted, and custom color swatches from images
- 📸 **Progressive Image Loading** - Thumbnail blur-up effect for faster perceived load times
- 🌀 **Parallax Effects** - Add depth with customizable parallax movement
- 🎞️ **Multi-Format Support** - Native GIF, WebP, and SVG rendering
- 🖼️ **Skeleton Placeholders** - Alternative placeholder type for modern skeleton loading
- 🎨 **Customizable Shimmer Direction** - Horizontal, Vertical, or Diagonal shimmer effects
- ♻️ **Intelligent Caching** - Per-request memory and disk cache policy control
- 🔧 **Advanced Image Processing** - Circle crop, blur transformations, and custom shapes

---

## ✨ Features

### Core Features
- 🚀 **Simple API** - Single composable function for all image loading needs
- 🎨 **Shape Support** - Built-in support for rounded corners, circles, and custom shapes
- ⚡ **Smart Loading States** - Automatic shimmer/skeleton placeholders and error handling
- 🔍 **Professional Zoom Engine** - Pinch-to-zoom, double-tap zoom, and smooth panning (1x to 4x)
- 🎭 **Fully Customizable UI** - Custom placeholders, error states, and callbacks
- 🔄 **Smooth Transitions** - Crossfade animations and shader reveal effects
- ⏱️ **Flicker-Free Shimmer** - Controlled minimum display time for consistent UX

---

## 📱 Sample App

The repository includes a comprehensive demo app showcasing all features:

- Interactive examples of all components
- Real-world usage patterns
- Performance demonstrations
- Feature comparisons

Clone the repository and run the app to explore!

---

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

### Ways to Contribute

1. **Report Bugs** - Open an issue with details and reproduction steps
2. **Suggest Features** - Share your ideas for improvements
3. **Submit PRs** - Fix bugs or add features
4. **Improve Docs** - Help make documentation better
5. **Share Feedback** - Let us know how you're using AsyncPic

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

**Made with ❤️ for the Android Compose Community**


</div>
