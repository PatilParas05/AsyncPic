package com.paraspatil.compose

sealed class ImageSource {
    data class Url(val value: String) : ImageSource()
}
