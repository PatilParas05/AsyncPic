package com.paraspatil.compose

enum class RevealType {
    DISSOLVE,
    PIXELATE,
    WIPE
}

sealed class AsyncPicTransition {
    object Standard : AsyncPicTransition()
    data class ShaderReveal(
        val type : RevealType = RevealType.DISSOLVE,
        val durationMillis : Int = 1000
    ): AsyncPicTransition()
}