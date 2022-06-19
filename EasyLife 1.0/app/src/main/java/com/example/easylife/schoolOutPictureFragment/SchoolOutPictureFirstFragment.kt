package com.example.easylife.schoolOutPictureFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.example.easylife.databinding.FragmentSchoolOutPictureFirstBinding
import com.example.easylife.inference.SecondOutPictureInferenceFirstFragment

class SchoolOutPictureFirstFragment : Fragment() {
    private lateinit var FragmentSchoolOutPictureFirstBindingBinding: FragmentSchoolOutPictureFirstBinding
    private lateinit var personName:String
    private lateinit var academyName:String
    private lateinit var myListener: MyListener
    private var textRow="民主"
    interface MyListener{
        fun sendMassage(personName:String,academyName:String,textRow:String);
    }
    var array:Array<String> = arrayOf("富强","民主","文明","和谐")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        FragmentSchoolOutPictureFirstBindingBinding= FragmentSchoolOutPictureFirstBinding.inflate(layoutInflater)
        personName=FragmentSchoolOutPictureFirstBindingBinding.editText.text.toString()
        academyName=FragmentSchoolOutPictureFirstBindingBinding.editText2.text.toString()
        FragmentSchoolOutPictureFirstBindingBinding.buttonEnter.setOnClickListener{
            personName=FragmentSchoolOutPictureFirstBindingBinding.editText.text.toString()
            academyName=FragmentSchoolOutPictureFirstBindingBinding.editText2.text.toString()
            myListener.sendMassage(personName,academyName,textRow)
        }
        FragmentSchoolOutPictureFirstBindingBinding.spinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                textRow=array[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
        return FragmentSchoolOutPictureFirstBindingBinding.root
    }
    fun getBinding(callback:SecondOutPictureInferenceFirstFragment){
        callback.get_message_from_Fragment(personName,academyName)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("fragment_onAttach",context.toString())
        myListener = activity as MyListener
    }

}