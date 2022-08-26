package com.example.news.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.news.R
import com.example.news.utilClass.Util

class FragmentsPageChangeList(context: Context,attributeSet: AttributeSet?,def:Int) :ConstraintLayout(context,attributeSet,def){
    constructor(context: Context):this(context,null)
    constructor(context: Context, attributeSet: AttributeSet?):this(context,attributeSet,0)
    private var lineSpace:LineSpace
    private var buttonOne: Button
    private var buttonTwo: Button
    private var buttonThree: Button
    private var length:Float=0f

    init {
        val inflate = LayoutInflater.from(context).inflate(R.layout.fragments_page_change_list, this, false)
        lineSpace=inflate.findViewById(R.id.lineSpaceToButton)
        buttonOne=inflate.findViewById(R.id.buttonOne)
        buttonTwo=inflate.findViewById(R.id.buttonTwo)
        buttonThree=inflate.findViewById(R.id.buttonThree)
        lineSpace.setLength(100.toFloat())
        buttonOne.text = "相关信息"
        buttonTwo.text = "聊天室"
        buttonThree.text = "庭审现场"
        addView(inflate)
        buttonOne.setOnClickListener {
            Util.action(lineSpace,"middle",lineSpace.getMiddle(),length/6,300, AccelerateDecelerateInterpolator())
        }
        buttonTwo.setOnClickListener {
            Util.action(lineSpace,"middle",lineSpace.getMiddle(),length/2,300, AccelerateDecelerateInterpolator())
        }
        buttonThree.setOnClickListener {
            Util.action(lineSpace,"middle",lineSpace.getMiddle(),length/6*5,300, AccelerateDecelerateInterpolator())
        }
    }
    fun setLength(float: Float){
        this.length=float
        Util.logDetail("length",length.toString())
        drawFirst()
    }
    fun setSpaceLength(float: Float){
        this.length=float
        lineSpace.setLength(float)
    }
    private fun setMiddle(float: Float){
        lineSpace.setMiddle(float)
    }
    private fun drawFirst(){
        setMiddle(length/6)
        lineSpace.setLength(length/6-50)
    }
    fun getButtonOne():Button{
        return this.buttonOne
    }
    fun getButtonTwo():Button{
        return this.buttonTwo
    }
    fun getButtonThree():Button{
        return this.buttonThree
    }
    fun getLineSpace():LineSpace{
        return this.lineSpace
    }
    fun getLength(): Float {
        return this.length
    }
}