package com.example.news.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.news.R

class CompetitionTextLineView(context: Context,attributeSet: AttributeSet?,def:Int):ConstraintLayout(context,attributeSet,def) {
    constructor(context: Context):this(context,null)
    constructor(context: Context,attributeSet: AttributeSet?):this(context,attributeSet,0)
    private var textViewSupport:TextView
    private var textViewOpposition:TextView
    private var lineSpace:CompetitionLineSpace
    init{
        val inflate= LayoutInflater.from(context).inflate(R.layout.competition_text_and_line,this,false)
        addView(inflate)
        textViewOpposition=inflate.findViewById(R.id.textViewOpposition)
        textViewSupport=inflate.findViewById(R.id.textViewSupport)
        textViewSupport.text="正方"
        textViewOpposition.text="反方"
        lineSpace=inflate.findViewById(R.id.completitionLineSpace)
    }
    fun setSupportText(text:String){
        this.textViewSupport.text=text
    }

    fun setOpposition(text:String){
        this.textViewOpposition.text=text
    }
    fun setLineData(support:Float,opposition:Float){
        this.lineSpace.setData(support,opposition)
    }
    fun getOppositionText(): TextView {
        return this.textViewOpposition
    }
    fun getSupportText(): TextView {
        return this.textViewSupport
    }
}