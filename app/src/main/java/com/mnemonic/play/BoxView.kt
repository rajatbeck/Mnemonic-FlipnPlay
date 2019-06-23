package com.mnemonic.play

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.os.Build
import android.support.annotation.Nullable
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.mnemonic.play.extension.toDp
import kotlin.random.Random


class BoxView : View {

    lateinit var paint: Paint
    lateinit var strokePaint: Paint
    lateinit var highLightPaint: Paint
    lateinit var rectArr: Array<Array<Rect>>
    lateinit var elementArr: Array<Array<Elements>>
    lateinit var color: ColorStateList
    lateinit var row: Number
    lateinit var col: Number
    var rectIndex = Pair(0, 0)
    var touching: Boolean = false
    var lastTouched: Pair<Int, Int> = Pair(-1, -1)
    var isMatch = false
    var flippedcount = 0
    var enable: Boolean = false
    lateinit var mListener: OnGameCompleteListener


    private var imageList: List<Int> = arrayListOf(
        R.drawable.ic_beach,
        R.drawable.ic_audio,
        R.drawable.ic_dollar,
        R.drawable.ic_car,
        R.drawable.ic_cloud,
        R.drawable.ic_camera,
        R.drawable.ic_android,
        R.drawable.ic_bluetooth,
        R.drawable.ic_cake,
        R.drawable.ic_bulb,
        R.drawable.ic_drink,
        R.drawable.ic_food,
        R.drawable.ic_flower,
        R.drawable.ic_heart,
        R.drawable.ic_hot,
        R.drawable.ic_location,
        R.drawable.ic_laptop,
        R.drawable.ic_paw,
        R.drawable.ic_lock,
        R.drawable.ic_sofa,
        R.drawable.ic_plane,
        R.drawable.ic_train,
        R.drawable.ic_star,
        R.drawable.ic_world
    ).shuffled()

    companion object {
        val TAG: String = BoxView::class.java.simpleName
    }

    constructor(context: Context?) : super(context)


    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs)
    }


    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(attrs)
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initView(attrs)
    }

    fun setOnGameCompleteListener(gameCompleteListener: OnGameCompleteListener) {
        mListener = gameCompleteListener
    }


    private fun initView(attrs: AttributeSet?) {

        val ta = context.obtainStyledAttributes(attrs, R.styleable.BoxView)
        color = ta.getColorStateList(R.styleable.BoxView_square_color)
        row = ta.getInt(R.styleable.BoxView_row, 0)
        col = ta.getInt(R.styleable.BoxView_column, 0)

        rectArr = Array(row.toInt()) { Array(col.toInt()) { Rect() } }
        elementArr = Array(row.toInt()) { Array(col.toInt()) { Elements() } }

        paint = Paint()
        paint.color = color.defaultColor
        paint.style = Paint.Style.FILL

        strokePaint = Paint()
        strokePaint.color = Color.DKGRAY
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = 2F

        highLightPaint = Paint()
        highLightPaint.color = ContextCompat.getColor(context, R.color.colorAliceBlue)
        highLightPaint.style = Paint.Style.FILL
        highLightPaint.isAntiAlias = true

        ta.recycle()
    }


    fun resetTheValues(level: String) {
        when (level) {
            "easy" -> {
                row = 2
                col = 2
            }
            "medium" -> {
                row = 4
                col = 4
            }
            else -> {
                row = 6
                col = 6
            }
        }
        rectArr = Array(row.toInt()) { Array(col.toInt()) { Rect() } }
        elementArr = Array(row.toInt()) { Array(col.toInt()) { Elements() } }
        imageList = imageList.shuffled()
        rectIndex = Pair(0, 0)
        touching = false
        lastTouched = Pair(-1, -1)
        isMatch = false
        flippedcount = 0
        enable = false
        val rowWidth = width / row.toInt()
        val colHeight = height / col.toInt()
        assignImages(row.toInt(), col.toInt(), elementArr)
        captureRect(rowWidth, colHeight, rectArr, _captureRect)
        requestLayout()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val rowWidth = width / row.toInt()
        val colHeight = height / col.toInt()
        assignImages(row.toInt(), col.toInt(), elementArr)
        captureRect(rowWidth, colHeight, rectArr, _captureRect)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (measuredWidth < measuredHeight) {
            setMeasuredDimension(measuredWidth, measuredWidth)
        } else {
            setMeasuredDimension(measuredHeight, measuredHeight)
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!enable) return true
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                rectIndex = getClickedRectangle(x, y)
                if (rectIndex.first == -1 && rectIndex.second == -1)
                    return true
                touching = true
                elementArr[rectIndex.first][rectIndex.second].flipped = true
                if ((lastTouched.first != -1 && lastTouched.second != -1) && !(lastTouched.first == rectIndex.first && lastTouched.second == rectIndex.second) && !(elementArr[rectIndex.first][rectIndex.second].matchFound))
                    checkLastClicked(lastTouched, rectIndex)
                invalidate(rectArr[rectIndex.first][rectIndex.second])
            }
            MotionEvent.ACTION_MOVE -> {

            }
            MotionEvent.ACTION_UP -> {
                if (rectIndex.first == -1 && rectIndex.second == -1)
                    return true
                touching = false
                if (elementArr[rectIndex.first][rectIndex.second].matchFound)
                    return true
                if (!isMatch) {
                    lastTouched = rectIndex
                } else {
                    elementArr[lastTouched.first][lastTouched.second].matchFound = true
                    elementArr[rectIndex.first][rectIndex.second].matchFound = true
                    lastTouched = Pair(-1, -1)
                    isMatch = false
                }
            }
            MotionEvent.ACTION_CANCEL -> {

            }

        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rowWidth = width / row.toInt()
        val colHeight = height / col.toInt()
        drawRect(rowWidth, colHeight, canvas, _drawRect)

    }

    private fun checkLastClicked(lastClick: Pair<Int, Int>, currentClick: Pair<Int, Int>) {
        if ((lastClick.first != currentClick.first || lastClick.second != currentClick.second) && elementArr[lastClick.first][lastClick.second].image != elementArr[currentClick.first][currentClick.second].image) {
            elementArr[lastClick.first][lastClick.second].flipped = false
        } else {
            flippedcount += 2
            if (flippedcount == (row.toInt() * col.toInt()))
                mListener.onCompleted()

            isMatch = true
        }
    }


    private fun assignImages(row: Int, col: Int, elmentArr: Array<Array<Elements>>) {
        val n = (row * col) / 2
        var pickedImageListOne = imageList.filterIndexed { index, i -> index < n }
        var pickedImageListTwo = imageList.filterIndexed { index, i -> index < n }
        for (i in 0 until row) {
            for (j in 0 until col) {
                if (pickedImageListOne.isNotEmpty()) {
                    elementArr[i][j] = Elements(pickedImageListOne[0], false)
                    pickedImageListOne = pickedImageListOne.filterIndexed { index, _ -> index != 0 }
                    pickedImageListOne = pickedImageListOne.shuffled()
                } else {
                    elementArr[i][j] = Elements(pickedImageListTwo[0], false)
                    pickedImageListTwo = pickedImageListTwo.filterIndexed { index, _ -> index != 0 }
                    pickedImageListTwo = pickedImageListTwo.shuffled()
                }
            }
        }

    }

    private fun getClickedRectangle(x: Float, y: Float): Pair<Int, Int> {
        rectArr.forEachIndexed { i, arrayOfRects ->
            for ((j, rect) in arrayOfRects.withIndex()) {
                if (rect.contains(x.toInt(), y.toInt()))
                    return Pair(i, j)
            }
        }
        return Pair(-1, -1)
    }


    private fun drawRect(rowWidth: Int, colHeight: Int, canvas: Canvas?, action: (Canvas?, Int, Int) -> Unit) =
        action(canvas, rowWidth, colHeight)

    private fun captureRect(
        rowWidth: Int,
        colHeight: Int,
        rectArray: Array<Array<Rect>>,
        action: (Array<Array<Rect>>, Int, Int) -> Unit
    ) = action(rectArray, rowWidth, colHeight)


    private fun reconcileSize(contentSize: Int, measureSpec: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (mode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> if (contentSize < specSize) {
                contentSize
            } else {
                specSize
            }
            MeasureSpec.UNSPECIFIED -> contentSize
            else -> contentSize
        }
    }

    private val _drawRect: (Canvas?, Int, Int) -> Unit = { canvas, rowWidth, colHeight ->
        var x: Float
        var y = 0F
        for (i in 0 until row.toInt()) {
            x = 0F
            for (j in 0 until col.toInt()) {
                val d = ContextCompat.getDrawable(context, elementArr[i][j].image)
                d?.setBounds(
                    rectArr[i][j].left + 12,
                    rectArr[i][j].top + 12,
                    rectArr[i][j].right - 12,
                    rectArr[i][j].bottom - 12
                )
                d?.draw(canvas)
                if (elementArr[i][j].flipped || (elementArr[rectIndex.first][rectIndex.second].flipped && rectIndex.first == i && rectIndex.second == j)) {
                    canvas?.drawRect(x, y, x + rowWidth.toFloat(), y + colHeight.toFloat(), highLightPaint)
                } else {
                    canvas?.drawRect(x, y, x + rowWidth.toFloat(), y + colHeight.toFloat(), paint)
                }
                canvas?.drawRect(x, y, x + rowWidth.toFloat(), y + colHeight.toFloat(), strokePaint)
                x = x.plus(rowWidth)

            }
            y = y.plus(colHeight)
        }
    }

    private val _captureRect: (Array<Array<Rect>>, Int, Int) -> Unit = { rectArr, rowWidth, colHeight ->
        var x: Float
        var y = 0F
        for (i in 0 until row.toInt()) {
            x = 0F
            for (j in 0 until col.toInt()) {
                rectArr[i][j] = Rect(x.toInt(), y.toInt(), x.toInt() + rowWidth, y.toInt() + colHeight)
                x = x.plus(rowWidth)
            }
            y = y.plus(colHeight)
        }

    }

    data class Elements(var image: Int = -1, var flipped: Boolean = false, var matchFound: Boolean = false)

    interface OnGameCompleteListener {

        fun onCompleted()
    }


}