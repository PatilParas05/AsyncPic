package com.paraspatil.compose

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import coil.size.Size
import coil.transform.Transformation

class CustomBlurTransformation(
    private val context: Context,
    private val radius: Float = 10f,
    private val sampling: Float = 1f
) : Transformation {
    override val cacheKey: String = "CustomBlurTransformation(radius=$radius,sampling=$sampling)"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val width = (input.width * sampling).toInt()
        val height = (input.height * sampling).toInt()

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        canvas.scale(sampling, sampling)
        val paint = Paint(Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG)
        canvas.drawBitmap(input, 0f, 0f, paint)

        return try {
            val rs = RenderScript.create(context)
            val inputAllocation = Allocation.createFromBitmap(rs, output)
            val outputAllocation = Allocation.createTyped(rs, inputAllocation.type)
            val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

            blurScript.setRadius(radius.coerceIn(0f, 25f))
            blurScript.setInput(inputAllocation)
            blurScript.forEach(outputAllocation)
            outputAllocation.copyTo(output)

            rs.destroy()
            output

        } catch (e: Exception) {
            output // return the scaled bitmap if blur fails
        }
    }
}
