# ComposeImageLoader

ComposeImageLoader is a Jetpack Compose library designed to simplify image loading, offering features like various image transformations, customizable placeholders, error state handling, and a convenient zoomable modifier. The project also includes a showcase app demonstrating the library's capabilities.

## Library Module: `image-loader-compose`

The core of this project is the `image-loader-compose` library.

### Features

*   **Unified Image Loading**: A central `AsyncImageLoader` composable for handling image requests.
*   **Multiple Image Sources**: Load images from URLs, `File` objects, or Drawable resource IDs using the flexible `ImageRequestData` model.
*   **Image Transformations**:
    *   `ImageTransformation.None`: Display the image as is.
    *   `ImageTransformation.Circle`: Crop the image into a circle, ideal for avatars.
    *   `ImageTransformation.RoundedCorners(dp)`: Apply rounded corners with a specified radius.
*   **Customizable Placeholders**:
    *   Use the built-in `DefaultShimmer()` for an animated loading placeholder.
    *   Provide your own custom `@Composable` function for a unique loading state.
*   **Customizable Error States**:
    *   Use the built-in `DefaultErrorIcon()` for a default error indicator.
    *   Provide your own custom `@Composable` function to handle image loading errors.
*   **Zoomable Modifier**: Easily add pinch-to-zoom, double-tap-to-zoom, and pan gestures to your images with the `.zoomable()` modifier.
*   **Built on Coil**: Leverages the power and efficiency of the [Coil](https://coil-kt.github.io/coil/) image loading library.

### Installation

To use `image-loader-compose` in your Android Jetpack Compose project, add the following dependency to your module's `build.gradle.kts` (or `build.gradle`) file:

```kotlin
// build.gradle.kts
dependencies {
    implementation("com.paraspatil.image_loader_compose:image-loader-compose:1.0.0")
    // Ensure you have Coil's compose artifact as well, if not already included
    implementation("io.coil-kt:coil-compose:2.5.0") // Or the latest version
}
```

```groovy
// build.gradle (Groovy)
dependencies {
    implementation 'com.paraspatil.image_loader_compose:image-loader-compose:1.0.0'
    // Ensure you have Coil's compose artifact as well, if not already included
    implementation 'io.coil-kt:coil-compose:2.5.0' // Or the latest version
}
```

**(Note:** You would need to publish your library to a Maven repository like Maven Central for the above dependency to work for others. If you are using it locally as a module, the dependency would be `implementation(project(":image-loader-compose"))`.)

### Usage

#### Basic Example

The primary component is `AsyncImageLoader`. You provide it with `ImageRequestData`:

```kotlin
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paraspatil.compose.AsyncImageLoader
import com.paraspatil.compose.ImageRequestData
import com.paraspatil.compose.ImageTransformation
import com.paraspatil.compose.DefaultShimmer // Optional: if using default shimmer

@Composable
fun MyImageDisplay() {
    AsyncImageLoader(
        data = ImageRequestData(
            url = "https://your.image.url/here.jpg",
            contentDescription = "An example image",
            transform = ImageTransformation.RoundedCorners(16.dp), // Apply rounded corners
            placeholder = { DefaultShimmer() } // Use the default shimmer
        ),
        modifier = Modifier.size(200.dp)
    )
}
```

#### Key Components to Import:

*   `com.paraspatil.compose.AsyncImageLoader`: The main composable for displaying images.
*   `com.paraspatil.compose.ImageRequestData`: The data class to define your image request.
*   `com.paraspatil.compose.ImageTransformation`: Sealed class for different image transformations.
*   `com.paraspatil.compose.DefaultShimmer`: A built-in shimmer placeholder.
*   `com.paraspatil.compose.DefaultErrorIcon`: A built-in error icon.
*   `com.paraspatil.compose.zoomable`: Modifier extension for zoom and pan functionality.

#### Image Sources in `ImageRequestData`

Provide only one of these per request:
*   `url: String?`
*   `file: File?`
*   `resId: Int?`

#### Transformations

Set the `transform` property in `ImageRequestData`:
*   `ImageTransformation.None`
*   `ImageTransformation.Circle`
*   `ImageTransformation.RoundedCorners(radius: Dp)`

#### Placeholders and Error States

Customize loading and error states within `ImageRequestData`:
*   `placeholder: @Composable () -> Unit = { DefaultShimmer() }`
*   `error: @Composable () -> Unit = { DefaultErrorIcon() }`
    You can replace `DefaultShimmer()` or `DefaultErrorIcon()` with your own composable functions.

#### Zoomable Modifier

Apply the `.zoomable()` modifier directly to the `AsyncImageLoader` for pinch-to-zoom, double-tap zoom, and pan gestures. Ensure the `contentScale` is appropriate (e.g., `ContentScale.Fit` often works well with `zoomable`).

```kotlin
import com.paraspatil.compose.zoomable
import androidx.compose.ui.layout.ContentScale

AsyncImageLoader(
    data = ImageRequestData(url = "..."),
    modifier = Modifier
        .fillMaxSize()
        .zoomable(), // Add this for zoom capabilities
    contentScale = ContentScale.Fit
)
```

## Showcase App (`app` module)

The `app` module in this project serves as a comprehensive demonstration of the `image-loader-compose` library. It showcases:
*   Various image transformations.
*   Default and custom placeholders.
*   Error handling.
*   The zoomable modifier in action in a full-screen viewer.
*   Different layout examples (Grid, List).

To run the demo, simply open the project in Android Studio and run the `app` configuration on an emulator or device.

---

Feel free to suggest any changes or additions!
