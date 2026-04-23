package com.paraspatil.compose

sealed class ImageSource {
    data class Url(val value: String) : ImageSource()
    data class Resources(val resId: Int) : ImageSource()

    data class Progressive(
        val finalUrl: String,
        val thumbnailUrl: String? = null,
        val blurHash: String? = null
    ): ImageSource()

    enum class PlaceholderType{
        SHIMMER,
        SKELETON,
        NONE
    }
    enum class ShimmerDirection{
        DIAGONAL,
        LTR,//left to right
        RTL,//right to left
        TTB,//top to bottom
        BTT//bottom to top
    }
}
