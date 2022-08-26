package com.example.news.repository

import androidx.lifecycle.liveData
import com.example.news.utilClass.Util
import com.example.news.utilClass.Util.await
import com.example.news.network.LoginNetwork
import kotlinx.coroutines.Dispatchers


object LoginRepository {
    fun getLoginData(username:String,password:String)=liveData(Dispatchers.IO){
        val result=try {

            val response=LoginNetwork.loginService.getLoginData(username,password).await()
            if(response.code==200){
                Util.logDetail("login=========",response.toString())
                Result.success(response)
            }
            else{
                Util.logDetail("@@@@@@@@@@")
                Result.failure(RuntimeException(response.message))
            }
        }catch (e:Exception){
            Util.logDetail("sssssssssss",e.toString())
            Result.failure(e)
        }
        emit(result)
    }

    fun getRegisterData(username:String,password:String)=liveData(Dispatchers.IO){
        val result=try {
            val response=LoginNetwork.loginService.getRegisterData(username,password).await()
            if(response.code==200){
                Util.logDetail("login=========",response.toString())
                Result.success("SUCCESS")
            }
            else{
                Util.logDetail("注册",response.toString())
                Result.failure(RuntimeException(response.message))
            }
        }catch (e:Exception){
            Util.logDetail("sssssssssss",e.toString())
            Result.failure(e)
        }
        emit(result)
    }

}