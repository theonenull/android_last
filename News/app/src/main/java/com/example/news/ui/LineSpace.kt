package com.example.news.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ImageView

@SuppressLint("AppCompatCustomView")
class LineSpace(context: Context, attributeSet: AttributeSet?, def:Int):
    ImageView(context,attributeSet,def) {
    constructor(context: Context):this(context,null)
    constructor(context: Context, attributeSet: AttributeSet?):this(context,attributeSet,0)
    private var lineStart=0f
    private var middle:Float=0f
    private var lineEnd=0f
    private var length=0f
    private var paint= Paint()
    fun setLineEnd(float: Float){
        this.lineEnd=float
        invalidate()
    }
    fun getLineEnd():Float{
        return lineEnd
    }
    fun setLength(float: Float){
        this.length=float
        invalidate()
    }
    fun setMiddle(float: Float){
        this.middle=float
        invalidate()
    }
    fun getMiddle():Float{
        return this.middle
    }
    override fun onDraw(canvas: Canvas?) {
        paint.strokeWidth=height.toFloat()
        paint.color = Color.BLUE
        canvas?.drawLine(middle-length/2,(height/2).toFloat(),middle+length/2,(height/2).toFloat(),paint)
        super.onDraw(canvas)
    }
}