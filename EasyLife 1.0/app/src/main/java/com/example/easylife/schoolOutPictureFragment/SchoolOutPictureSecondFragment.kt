package com.example.easylife.schoolOutPictureFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.Time
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.easylife.R
import com.example.easylife.databinding.FragmentSchoolOutPictureSecondBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import kotlin.concurrent.thread
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import androidx.annotation.RequiresApi
import com.example.easylife.CLass.QRCodeUtil
import android.animation.ObjectAnimator
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.LinearInterpolator


class SchoolOutPictureSecondFragment() : Fragment() {
    private lateinit var binding: FragmentSchoolOutPictureSecondBinding
    private lateinit var personName:String
    private lateinit var academyName:String
    private lateinit var textRow:String
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSchoolOutPictureSecondBinding.inflate(layoutInflater)
        val bundle = arguments
        if (bundle != null) {
            personName= bundle.getString("personName").toString()
            academyName= bundle.getString("academyName").toString()
            textRow = bundle.getString("textRow").toString()
            binding.academyName.text ="部门：$academyName"
            binding.personName.text="姓名：$personName"
            binding.textView.text = "$textRow"
            thread {
                while (true){

                    val animator: ObjectAnimator = ObjectAnimator.ofFloat(binding.textView, "TranslationX", 950f,-600f )
                    animator.duration = 4000
                    animator.interpolator = LinearInterpolator()
                    activity?.runOnUiThread {
                        animator.start()
                    }
                    Thread.sleep(4100)
                }
            }
        }
        thread {
            while (true){
                Thread.sleep(1000)
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("MM月dd日")
                val formatter2=DateTimeFormatter.ofPattern("ss")
                val formatter3=DateTimeFormatter.ofPattern("HH:mm")
                val formatted = current.format(formatter)
                val formatted2 = current.format(formatter2)
                val formatted3 = current.format(formatter3)
                binding.textViewHourMinute.text=formatted3.toString()
                binding.textViewMonthDay.text=formatted.toString()
                binding.textViewSecond.text=formatted2.toString()
                Log.d("time________",formatted.toString())
            }
        }
        val QRCodeUtil=QRCodeUtil()
        Log.d("ssssssss",binding.outPicture.width.toString())
        val textView=binding.textView
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);
        textView.setSelected(true);
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);

        binding.outPicture.setImageBitmap(QRCodeUtil.createQRCodeBitmap("学院：$academyName 姓名：$personName",
            100,100,
                                "UTF-8","L","0", Color.BLACK,Color.WHITE));

        return binding.root
    }

}