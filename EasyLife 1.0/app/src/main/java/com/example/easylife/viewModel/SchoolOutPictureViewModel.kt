package com.example.easylife.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SchoolOutPictureViewModel(personName:String,academyName:String): ViewModel() {
    val personName:LiveData<String>
        get() = _personName
    val academyName:LiveData<String>
        get()= _academyName

    private val _personName=MutableLiveData<String>()
    private val _academyName=MutableLiveData<String>()

    init {
        _academyName.value=academyName
        _personName.value=personName
    }

    fun dataActivitySet(personName: String,academyName: String){
        val tempPersonName=personName
        val tempAcademyName=academyName
        _personName.value=tempPersonName
        _academyName.value=tempAcademyName
    }
}