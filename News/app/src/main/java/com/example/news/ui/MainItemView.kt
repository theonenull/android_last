package com.example.news.ui

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import androidx.cardview.widget.CardView

class MainItemView(context: Context,attributeSet: AttributeSet?,def:Int) : CardView(context,attributeSet,def) {
    private val paint= Paint()
    private var bool:Boolean=true
    constructor(context: Context):this(context,null)
    constructor(context: Context,attributes: AttributeSet?):this(context,attributes,0)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if(bool){
            setMeasuredDimension(widthMeasureSpec,heightMeasureSpec/2);
        }
        else{
            setMeasuredDimension(100,100);
        }
    }
    fun setS(boolean: Boolean){
        bool=boolean
    }
}