package com.example.news.viewModel

import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.news.classes.network.getUserDataByIdNetwork.User
import com.example.news.repository.PersonRepository
import java.io.File

class PersonViewModel(userID: String): ViewModel() {

    val userId=MutableLiveData<String>()
    fun refreshUserId(userId: String){
        this.userId.postValue(userId)
    }
    val userData=Transformations.switchMap(userId){
        PersonRepository.getUserData(it)
    }


    private val massageForChange=MutableLiveData<MassageToChange>()
    fun refreshMassageForChange(data: MassageToChange){
        this.massageForChange.postValue(data)
    }
    val refreshMassageForChangeBackData=Transformations.switchMap(massageForChange){
        PersonRepository.sendUserChangeMassage(it,userID)
    }


    val file=MutableLiveData<File>()
    fun refreshFile(file: File){
        this.file.postValue(file)
    }
    val refreshFileBackData=Transformations.switchMap(file){
        PersonRepository.sendUserPicture(it,userID)
    }

    class MassageToChange(val phone:String,val age:String,val nickname: String,val qq:String){}
}