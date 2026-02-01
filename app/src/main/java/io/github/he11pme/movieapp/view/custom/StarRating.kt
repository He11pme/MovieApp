package io.github.he11pme.movieapp.view.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.PathParser
import io.github.he11pme.movieapp.utils.extensions.dp
import kotlin.math.min
import androidx.core.graphics.withSave
import io.github.he11pme.movieapp.R

class StarRating @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet) {

    private val starPath = PathParser.createPathFromPathData(PATH_DATA)
    private val scaledStarPath = Path(starPath)
    private val countStar: Int
    private val paddingStar: Float
    private var starSize: Float
    var rate: Float = 0f
        set(value) {
            field = value
            startAnimation(getRateWidth())
        }

    private var ratingWidth: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var emptyPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 5f
        isAntiAlias = true
        color = Color.LTGRAY
        alpha = 60
    }

    private var fillPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 5f
        isAntiAlias = true
        color = Color.YELLOW
    }

    init {
        val attributes =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.StarRating, 0, 0)

        try {
            starSize = attributes.getDimension(R.styleable.StarRating_sizeStar, 32.dp.toFloat())
            paddingStar =
                attributes.getDimension(R.styleable.StarRating_paddingStar, 5.dp.toFloat())
            countStar = attributes.getInt(R.styleable.StarRating_countStar, 5)
            rate = attributes.getFloat(R.styleable.StarRating_rate, 0f)
        } finally {
            attributes.recycle()
        }

        ratingWidth = getRateWidth()
    }

    private fun getRateWidth(): Float {
        val countFillStar = countStar * rate / 10
        val countFullStar = countFillStar.toInt()
        val remainder = countFillStar - countFullStar

        return countFullStar * (starSize + paddingStar) + remainder * starSize
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desiredHeight = starSize
        val desiredWidth = (starSize * countStar + (countStar - 1) * paddingStar)

        val width = chooseDimension(widthMeasureSpec, desiredWidth.toInt())
        val height = chooseDimension(heightMeasureSpec, desiredHeight.toInt())

        setMeasuredDimension(width, height)
    }

    private fun chooseDimension(spec: Int, desiredSize: Int): Int {
        val size = MeasureSpec.getSize(spec)

        return when (MeasureSpec.getMode(spec)) {
            MeasureSpec.AT_MOST -> min(size, desiredSize)
            MeasureSpec.EXACTLY -> size
            else -> desiredSize
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val scaleStar = starSize / VIEWPORT_SIDE

        scaledStarPath.transform(Matrix().apply { setScale(scaleStar, scaleStar) })

    }

    override fun onDraw(canvas: Canvas) {
        canvas.withSave {
            repeat(countStar) {
                drawPath(scaledStarPath, emptyPaint)
                translate((height + paddingStar), 0f)
            }
        }
        canvas.withSave {
            clipRect(0f, 0f, ratingWidth, height.toFloat())

            repeat(countStar) {
                drawPath(scaledStarPath, fillPaint)
                translate((height + paddingStar), 0f)
            }
        }
    }

    fun startAnimation(toValue: Float) {
        ValueAnimator.ofFloat(0f, toValue).apply {
            duration = 1000
            addUpdateListener { ratingWidth = it.animatedValue as Float }
            start()
        }
    }

    companion object {
        private const val PATH_DATA =
            "M376.13,328.54l26.02,151.74c0.03,0.16 0.04,0.22 -0.15,0.36c-0.18,0.13 -0.24,0.1 -0.39,0.03l-136.27,-71.64c-5.85,-3.08 -12.84,-3.08 -18.7,-0l-136.27,71.64c-0.14,0.08 -0.2,0.1 -0.39,-0.03c-0.18,-0.14 -0.17,-0.2 -0.15,-0.36l26.02,-151.74c1.12,-6.52 -1.04,-13.16 -5.78,-17.78L19.85,203.29c-0.12,-0.11 -0.16,-0.16 -0.09,-0.38c0.07,-0.22 0.14,-0.23 0.3,-0.25l152.36,-22.14c6.54,-0.95 12.2,-5.06 15.13,-10.99l68.14,-138.06c0.07,-0.14 0.1,-0.2 0.33,-0.2c0.23,0 0.26,0.06 0.33,0.2l68.14,138.06c2.93,5.93 8.58,10.04 15.12,10.99l152.36,22.14c0.16,0.02 0.23,0.03 0.3,0.25c0.07,0.22 0.02,0.26 -0.09,0.38L381.91,310.76C377.17,315.37 375.01,322.02 376.13,328.54z"

        private const val VIEWPORT_SIDE = 512f
    }

}