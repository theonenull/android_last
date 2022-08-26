package com.example.news.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

import com.example.news.repository.LoginRepository
import kotlinx.coroutines.*


class LoginViewModel(): ViewModel() {
    private var key:String?=null
    private val job=Job()

    private val userLoginData=MutableLiveData<UserLoginData>()
    val loginBackData = Transformations.switchMap(userLoginData){
        LoginRepository.getLoginData(it.userName,it.passWord)
    }
    fun refreshUserLoginData(userName:String,passWord: String){
        this.userLoginData.postValue(UserLoginData(userName,passWord))
    }
//    fun refreshKey(){
//        CoroutineScope(job).launch {
//            key = try {
//                Util.logDetail("wwwww",ServiceCreator.create<TotalRetrofit>().getPublicKey().await().data.toString())
//                ServiceCreator.create<TotalRetrofit>().getPublicKey().await().data
//            } catch (e:Exception){
//                withContext(Dispatchers.Main){
//                    easyToast("网络出错")
//                    Util.logDetail("ssssssss",e.toString())
//                }
//                null
//            }
//        }
//    }

    private val userRegisterData=MutableLiveData<UserLoginData>()
    val registerBackData = Transformations.switchMap(userRegisterData){
        LoginRepository.getRegisterData(it.userName,it.passWord)
    }
    fun refreshUserRegisterData(userName:String,passWord: String){
        this.userRegisterData.postValue(UserLoginData(userName,passWord))
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
    class UserLoginData(val userName: String,val passWord: String)
    class RegisterData(val userName: String,val passWord: String,val validation: String)
}