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
import android.util.AttributeSet
import android.util.Log
import com.mnemonic.play.extension.toDp


class BoxView : View {

    lateinit var paint: Paint
    lateinit var strokePaint: Paint
    lateinit var rectArr: Array<Array<Rect>>
    lateinit var color: ColorStateList
    lateinit var row: Number
    lateinit var col: Number


    var imageList: List<Int> = arrayListOf(
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
    )

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


    private fun initView(attrs: AttributeSet?) {

        val ta = context.obtainStyledAttributes(attrs, R.styleable.BoxView)
        color = ta.getColorStateList(R.styleable.BoxView_square_color)
        row = ta.getInt(R.styleable.BoxView_row, 0)
        col = ta.getInt(R.styleable.BoxView_column, 0)

        rectArr = Array(row.toInt()) { Array(col.toInt()) { Rect() } }

        paint = Paint()
        paint.color = color.defaultColor
        paint.style = Paint.Style.FILL

        strokePaint = Paint()
        strokePaint.color = Color.DKGRAY
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = 2F

        ta.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val rowWidth = width / row.toInt()
        val colHeight = height / col.toInt()
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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val rowWidth = width / row.toInt()
        val colHeight = height / col.toInt()
        drawRect(rowWidth, colHeight, canvas, _drawRect)
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
                canvas?.drawRect(x, y, x + rowWidth.toFloat(), y + colHeight.toFloat(), paint)
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


}