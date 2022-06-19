package com.example.easylife

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.easylife.ViewModelFactory.SchoolOutPictureViewModelFactory
import com.example.easylife.databinding.ActivitySchoolOutPictureBinding
import com.example.easylife.databinding.FragmentSchoolOutPictureFirstBinding
import com.example.easylife.schoolOutPictureFragment.SchoolOutPictureFirstFragment
import com.example.easylife.schoolOutPictureFragment.SchoolOutPictureSecondFragment
import com.example.easylife.viewModel.SchoolOutPictureViewModel


class SchoolOutPictureActivity : AppCompatActivity(),SchoolOutPictureFirstFragment.MyListener {
    lateinit var sp:SharedPreferences
    lateinit var SecondFragmentManager:FragmentManager
    lateinit var transaction: FragmentTransaction
    private lateinit var bindingSchoolOutPicture:ActivitySchoolOutPictureBinding
    private lateinit var viewModel:SchoolOutPictureViewModel
    override fun sendMassage(personName:String, academyName:String,textRow:String){
        viewModel.dataActivitySet(personName,academyName)
        this.enterPicture(personName,academyName,textRow)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sp=getPreferences(Context.MODE_PRIVATE)
        val personName=sp.getString("personName","刘明").toString()
        val academyName=sp.getString("academyName","海洋学院").toString()
        bindingSchoolOutPicture= ActivitySchoolOutPictureBinding.inflate(layoutInflater)
        val root= bindingSchoolOutPicture.root
        setContentView(root)
        SecondFragmentManager = supportFragmentManager
        transaction=SecondFragmentManager.beginTransaction()
        val fragment=SchoolOutPictureFirstFragment()
        transaction.replace(R.id.school_picture_first_fragment,fragment)
        transaction.commit()
        viewModel=ViewModelProvider(this, SchoolOutPictureViewModelFactory(personName,academyName)).get(SchoolOutPictureViewModel::class.java)
    }
    fun enterPicture(personName: String,academyName: String,textRow: String){
        SecondFragmentManager = supportFragmentManager
        transaction=SecondFragmentManager.beginTransaction()
        val fragmentSecond=SchoolOutPictureSecondFragment()
        val bundle = Bundle()
        bundle.putString("personName",personName)
        bundle.putString("academyName",academyName)
        bundle.putString("textRow",textRow)
        fragmentSecond.arguments = bundle
        transaction.replace(R.id.school_picture_first_fragment,fragmentSecond)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}