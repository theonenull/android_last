package com.example.news.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.EditText
import android.widget.ImageView

@SuppressLint("AppCompatCustomView")
class SpaceView(context: Context,attributeSet: AttributeSet?,def:Int):ImageView(context,attributeSet,def) {
    constructor(context: Context):this(context,null)
    constructor(context: Context, attributeSet: AttributeSet?):this(context,attributeSet,0)
    private var lineEnd=0f
    private var paint= Paint()
    fun setLineEnd(float: Float){
        this.lineEnd=float
        invalidate()
    }
    fun getLineEnd():Float{
        return lineEnd
    }
    override fun onDraw(canvas: Canvas?) {
        paint.strokeWidth=height.toFloat()
        paint.color = Color.BLUE
        canvas?.drawLine(0f,(height/2).toFloat(),lineEnd,(height/2).toFloat(),paint)
            super.onDraw(canvas)
    }
}