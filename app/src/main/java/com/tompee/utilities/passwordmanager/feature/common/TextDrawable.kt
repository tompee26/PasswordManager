package com.tompee.utilities.passwordmanager.feature.common

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.NonNull


class TextDrawable(res: Resources, private val mText: CharSequence, isBold: Boolean) : Drawable() {
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val intrinsicWidth: Int
    private val intrinsicHeight: Int

    companion object {
        private const val DEFAULT_COLOR = Color.WHITE
        private const val DEFAULT_TEXT_SIZE = 18
    }

    init {
        paint.color = DEFAULT_COLOR
        if (isBold) {
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        paint.textAlign = Paint.Align.CENTER
        val textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            DEFAULT_TEXT_SIZE.toFloat(), res.displayMetrics
        )
        paint.textSize = textSize
        intrinsicWidth = (paint.measureText(mText, 0, mText.length) + .5).toInt()
        intrinsicHeight = paint.getFontMetricsInt(null)
    }

    override fun draw(@NonNull canvas: Canvas) {
        val bounds = bounds
        canvas.drawText(
            mText.toString(),
            (bounds.width() / 2).toFloat(),
            bounds.height() / 2 - (paint.descent() + paint.ascent()) / 2,
            paint
        )
    }

    override fun getOpacity(): Int {
        return paint.alpha
    }

    override fun getIntrinsicWidth(): Int {
        return intrinsicWidth
    }

    override fun getIntrinsicHeight(): Int {
        return intrinsicHeight
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(filter: ColorFilter?) {
        paint.colorFilter = filter
    }
}
