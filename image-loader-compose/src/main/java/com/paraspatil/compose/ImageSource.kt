package com.paraspatil.compose

sealed class ImageSource {
    data class Url(val value: String) : ImageSource()
    data class Resources(val resId: Int) : ImageSource()

    data class Progressive(
        val finalUrl: String,
        val thumbnailUrl: String? = null,
        val blurHash: String? = null
    ): ImageSource()
}
