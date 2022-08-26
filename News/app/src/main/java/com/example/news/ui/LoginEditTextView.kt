package com.example.news.ui


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.text.method.TransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.example.news.R


@SuppressLint("AppCompatCustomView")
class LoginEditTextView(context: Context, attributeSet: AttributeSet?, def:Int):
    ConstraintLayout(context,attributeSet,def) {
    constructor(context: Context):this(context,null)
    constructor(context: Context, attributeSet: AttributeSet?):this(context,attributeSet,0)
    private var imageViewIcon = 0
    private var editViewInputHint: String? = null
    private var editViewIsPass = false
    private var inputType :String? = null
    var imageView: ImageView
    private var editText: EditText
    private var spaceView:SpaceView
    private var text:String=""
    init{
        //加载.
        val typedArray:TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.LoginEditTextView)
        imageViewIcon = typedArray.getResourceId(R.styleable.LoginEditTextView_input_icon, R.mipmap.ic_launcher)
        editViewInputHint = typedArray.getString(R.styleable.LoginEditTextView_input_hint)
        editViewIsPass = typedArray.getBoolean(R.styleable.LoginEditTextView_is_pass, false)
        inputType=typedArray.getString(R.styleable.LoginEditTextView_input_type)
        typedArray.recycle()
        //释放资源
        val inflate = LayoutInflater.from(context).inflate(com.example.news.R.layout.login_edit_text, this, false)
        imageView = inflate.findViewById(R.id.icon)
        editText = inflate.findViewById(R.id.text)
        spaceView = inflate.findViewById(R.id.spaceView)
        editText.hint=editViewInputHint
        if(inputType=="password"){
            editText.inputType= InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.inputType = 129;
            editText.transformationMethod = PasswordTransformationMethod
            .getInstance()
        }
        else if(inputType=="email"){
            editText.inputType= InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
        }
        imageView.setImageResource(imageViewIcon)
        editText.setSingleLine()
        editText.setSelection(editText.text.length);
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // 此处为得到焦点时的处理内容
                val animator: ObjectAnimator = ObjectAnimator.ofFloat(spaceView,"lineEnd",0f, width.toFloat())
                animator.duration = 500
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.start()
            } else {
                // 此处为失去焦点时的处理内容
                val animator: ObjectAnimator = ObjectAnimator.ofFloat(spaceView,"lineEnd",width.toFloat(),0f )
                animator.duration = 500
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.start()
            }
        }
        editText.addTextChangedListener {
            this.text=editText.text.toString()
        }
        addView(inflate);

    }
    fun getTheText():String{
        return editText.text.toString()
    }
    fun setText(text:String){
        this.editText.setText(text)
    }

}