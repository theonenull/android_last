package com.example.news.repository

import androidx.lifecycle.liveData
import com.example.news.utilClass.Util
import com.example.news.utilClass.Util.await
import com.example.news.classes.network.getUserDataByIdNetwork.User
import com.example.news.keyValue.USERDATA
import com.example.news.network.PersonNetWork
import com.example.news.network.PersonNetWork.personService
import com.example.news.viewModel.PersonViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.RuntimeException

object PersonRepository {
    fun getUserData(userId:String)= liveData(Dispatchers.IO){
        val result=try{
            val userdata=personService.getUserDataById(userId).await()
            Result.success(userdata.data!!.user!!)
        }
        catch (b:Exception){
            Result.success(displayTheUserData())
        }
        emit(result)
    }

    fun sendUserPicture(file: File,userId: String)= liveData(Dispatchers.IO){
        val result=try {
            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
            val part =
                MultipartBody.Part.createFormData("file", "file.jpg", requestFile)
            val response=
                PersonNetWork.personService.sendUserChangeImage(part,userId).await()
            Util.logDetail("?????????",response.code)
            if(response.code=="200"){
                Result.success("")
            } else {
                Result.failure(RuntimeException(response.massage.toString()))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun sendUserChangeMassage(data: PersonViewModel.MassageToChange,userId: String)= liveData(Dispatchers.IO){
        val result=try {
            val response=
                PersonNetWork.personService.sendUserChangeMassage(data.age,data.nickname,data.phone,data.qq,userId).await()
            if(response.code=="200"){
                Result.success("")
            } else {
                Result.failure(RuntimeException(response.massage.toString()))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    private fun displayTheUserData(): User {
        return User(
            Util.getSharePreferencesString(null, USERDATA.PLACE, USERDATA.USER_ID).toInt(),
            Util.getSharePreferencesString(null, USERDATA.PLACE, USERDATA.ROLE),
            null,
            null,
            Util.getSharePreferencesString(null, USERDATA.PLACE, USERDATA.NICKNAME),
            Util.getSharePreferencesString(null, USERDATA.PLACE, USERDATA.SALT),
            Util.getSharePreferencesString(null, USERDATA.PLACE, USERDATA.IMAGE),
            Util.getSharePreferencesString(null, USERDATA.PLACE, USERDATA.AGE)
        )
    }
}