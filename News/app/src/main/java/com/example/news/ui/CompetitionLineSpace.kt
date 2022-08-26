package com.example.news.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ImageView

@SuppressLint("AppCompatCustomView")
class CompetitionLineSpace(context: Context, attributeSet: AttributeSet?, def:Int): ImageView(context,attributeSet,def) {
    constructor(context: Context):this(context,null)
    constructor(context: Context,attributeSet: AttributeSet?):this(context,attributeSet,0)
    private var paint= Paint()
    private var support:Float=1f
    private var opposition:Float=1f
    private var supportOfAllWidth:Float=0.5f
    private var oppositionOfAllWidth:Float=0.5f
    override fun onDraw(canvas: Canvas?) {
        val halfHeight=(height/2).toFloat()
        paint.strokeWidth=height.toFloat()
        paint.color = Color.RED
        canvas?.drawLine(0f,halfHeight,supportOfAllWidth*width,halfHeight,paint)
        paint.color = Color.BLUE
        canvas?.drawLine(width.toFloat(),halfHeight,width-oppositionOfAllWidth*width,halfHeight,paint)
        super.onDraw(canvas)
    }
    fun setData(support: Float,opposition: Float){
        this.support=support
        this.opposition=opposition
        if(support==0f&&opposition==0f){
            this.oppositionOfAllWidth=0.5f
            this.supportOfAllWidth=0.5f
            invalidate()
            return
        }
        if(support==0f){
            this.oppositionOfAllWidth=1f
            this.supportOfAllWidth=0f
            invalidate()
            return
        }
        if(opposition==0f){
            this.oppositionOfAllWidth=0f
            this.supportOfAllWidth=1f
            invalidate()
            return
        }
        this.oppositionOfAllWidth=opposition/(support+opposition)
        this.support=support/(support+opposition)
        invalidate()
    }
}