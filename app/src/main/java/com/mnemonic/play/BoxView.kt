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
        strokePaint.color = Color.WHITE
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = 2F

        ta.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if(measuredWidth<measuredHeight) {
            setMeasuredDimension(measuredWidth, measuredWidth)
        }else{
            setMeasuredDimension(measuredHeight, measuredHeight)
        }

    }

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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val rowWidth = width / row.toInt()
        val colHeight = height / col.toInt()

        var x: Float
        var y = 0F
        for (i in 1..rowWidth) {
            x = 0F
            for (j in 1..colHeight) {
                canvas?.drawRect(x, y, x + rowWidth.toFloat(), y + colHeight.toFloat(), paint)
                canvas?.drawRect(x, y, x + rowWidth.toFloat(), y + colHeight.toFloat(), strokePaint)
                x = x.plus(rowWidth)
            }
            y = y.plus(colHeight)
        }


    }


}