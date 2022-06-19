package com.example.easylife.SelfView

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

public class MyTextView(context: Context, attrs: AttributeSet?, defStyleAttr:Int) : androidx.appcompat.widget.AppCompatTextView(context,attrs,defStyleAttr) {
    constructor(context: Context):this(context,null)
    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,0)
    override fun isFocused(): Boolean {//必须重写，且返回值是true，表示始终获取焦点
        return true
    }
}