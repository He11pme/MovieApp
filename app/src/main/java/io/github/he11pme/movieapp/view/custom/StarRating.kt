package io.github.he11pme.movieapp.view.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.PathParser
import io.github.he11pme.movieapp.utils.extensions.dp
import kotlin.math.min
import androidx.core.graphics.withSave
import io.github.he11pme.movieapp.R
import androidx.core.graphics.withTranslation

class StarRating @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet) {

    private val starPath = PathParser.createPathFromPathData(PATH_DATA)
    private val scaledStarPath = Path(starPath)

    private var isStaticPictureDrawn = false
    private lateinit var bitmap: Bitmap
    private lateinit var staticCanvas: Canvas

    private val countStar: Int
    private val paddingStar: Float
    private val paddingRate: Float
    private var starSize: Float
    private var rateWidth: Float
    private val emptyStarColor: Int
    private val fillStarColor: Int
    private val ratingColor: Int
    private val emptyStarAlpha: Float

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

    private var emptyPaint = Paint()
    private var fillPaint = Paint()
    private var textPaint = Paint()

    init {
        val attributes =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.StarRating, 0, 0)

        try {
            starSize =
                attributes.getDimension(R.styleable.StarRating_sizeStar, 32.dp.toFloat())

            paddingStar =
                attributes.getDimension(R.styleable.StarRating_paddingStar, 5.dp.toFloat())
            paddingRate =
                attributes.getDimension(R.styleable.StarRating_paddingRate, 5.dp.toFloat())

            countStar = attributes.getInt(R.styleable.StarRating_countStar, 5)
            rate = attributes.getFloat(R.styleable.StarRating_rate, 0f)
            emptyStarColor = attributes.getColor(
                R.styleable.StarRating_emptyStarColor,
                ContextCompat.getColor(context, R.color.black)
            )
            emptyStarAlpha = attributes.getFloat(R.styleable.StarRating_emptyStarAlpha, 0.5f)
            fillStarColor = attributes.getColor(
                R.styleable.StarRating_fillStarColor,
                ContextCompat.getColor(context, R.color.yellow_A200)
            )
            ratingColor = attributes.getColor(
                R.styleable.StarRating_ratingColor,
                ContextCompat.getColor(context, R.color.black)
            )
        } finally {
            attributes.recycle()
        }

        initPaint()
        scaleStarPath()

        rateWidth = textPaint.measureText(rate.toString())
    }

    private fun initPaint() {
        emptyPaint.apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 5f
            color = emptyStarColor
            alpha = (emptyStarAlpha * 100).toInt()
            isAntiAlias = true
        }

        fillPaint.apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 5f
            color = fillStarColor
            isAntiAlias = true
        }

        textPaint.apply {
            style = Paint.Style.FILL
            color = ratingColor
            textSize = starSize
            isAntiAlias = true
        }
    }

    private fun scaleStarPath() {
        val scale = starSize / VIEWPORT

        scaledStarPath.transform(Matrix().apply { setScale(scale, scale) })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desiredHeight = starSize
        val desiredWidth =
            starSize * countStar + (countStar - 1) * paddingStar + rateWidth + paddingRate

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

    override fun onDraw(canvas: Canvas) {

        if (!isStaticPictureDrawn) drawStaticPicture()

        canvas.drawBitmap(bitmap, 0f, 0f, null)

        drawStarsAfterRating(canvas, fillPaint) {
            it.clipRect(0f, 0f, ratingWidth, height.toFloat())
        }
    }

    private fun drawStaticPicture() {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        staticCanvas = Canvas(bitmap)

        staticCanvas.drawText(rate.toString(), 0f, -textPaint.fontMetrics.ascent, textPaint)

        drawStarsAfterRating(staticCanvas, emptyPaint)

        isStaticPictureDrawn = true
    }

    private fun drawStarsAfterRating(canvas: Canvas, paint: Paint, doSetUpCanvas: ((Canvas) -> Unit)? = null) {
        canvas.withTranslation(rateWidth + paddingRate, 0f) {
            withSave {
                doSetUpCanvas?.invoke(this)
                repeat(countStar) {
                    drawPath(scaledStarPath, paint)
                    translate((height + paddingStar), 0f)
                }
            }
        }
    }

    private fun getRateWidth(): Float {
        val countFillStars = countStar * rate / 10

        return starSize * countFillStars + paddingStar * countFillStars.toInt()
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

        private const val VIEWPORT = 512f
    }

}