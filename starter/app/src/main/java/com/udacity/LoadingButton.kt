package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var textXPos = 0f
    private var textYPos = 0f
    private var progress = 0
    private var degrees = 0

    private val paint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = context.resources.getDimension(R.dimen.default_text_size)
        color = context.getColor(R.color.white)
    }

    private var rectAnimator = ValueAnimator()
    private var circleAnimator = ValueAnimator()

    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, new ->
        when(new) {
            ButtonState.Loading -> {
                circleAnimator = ValueAnimator.ofInt(0, widthSize).apply {
                    duration = 500
                    addUpdateListener { animator ->
                        degrees = animatedValue as Int
                        animator.repeatCount = ValueAnimator.INFINITE
                        animator.repeatMode = ValueAnimator.RESTART
                        invalidate()
                    }
                    start()
                }

                rectAnimator = ValueAnimator.ofInt(0, 360).apply {
                    duration = 500
                    addUpdateListener { animator ->
                        progress = animatedValue as Int
                        animator.repeatCount = ValueAnimator.INFINITE
                        animator.repeatMode = ValueAnimator.RESTART
                        invalidate()
                    }
                    start()
                }
            }
            ButtonState.Completed -> {
                circleAnimator.end()
                rectAnimator.end()
                degrees = 0
                progress = 0
                invalidate()
            }
        }
    }

    init {
        isClickable = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //Get the center position for the text
        textXPos = width / 2f
        textYPos = height / 2 - (paint.descent() + paint.ascent()) / 2
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(ContextCompat.getColor(context, R.color.colorAccent))

        paint.color = context.getColor(R.color.colorPrimaryDark)
        canvas?.drawRect(0f, 0f, progress.toFloat(), heightSize.toFloat(), paint)

        paint.color= context.getColor(R.color.white)
        canvas?.drawArc(
            (widthSize - 200f), (heightSize / 2) - 50f, (widthSize - 100f),
            (heightSize / 2) + 50f, 0F, degrees.toFloat(), true, paint
        )

        paint.color = context.getColor(R.color.white)
        val buttonText = when(buttonState) {
            ButtonState.Loading -> context.getString(R.string.button_loading)
            else -> context.getString(R.string.button_name)
        }
        canvas?.drawText(buttonText, textXPos, textYPos, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}