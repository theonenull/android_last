package com.example.easylife.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.easylife.viewModel.SchoolOutPictureViewModel

class SchoolOutPictureViewModelFactory(private val personName:String,private val academyName:String) :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SchoolOutPictureViewModel(personName,academyName) as T
    }
}