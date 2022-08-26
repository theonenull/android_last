package com.example.news.repository

import androidx.lifecycle.liveData
import com.example.news.keyValue.FRAG
import com.example.news.utilClass.Util
import com.example.news.utilClass.Util.await
import com.example.news.network.RecommendDetailNetwork
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object RecommendDetailRepository {
    fun getTrailData(judgeId: String) = liveData(Dispatchers.IO){
        val result=try {
            val response=RecommendDetailNetwork.recommendDetailService.getTrialDetail(judgeId).await()
            if(response.code.toString()=="200"){
                Result.success(response.data)
            } else {
                Result.failure(RuntimeException(response.message))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun postVote(flag:String,userId: String,id:String) = liveData(Dispatchers.IO){
        val result=try {
            val response=RecommendDetailNetwork.recommendDetailService.postVote(flag,id,userId).await()
            if(response.code.toString()=="200"){
                Result.success("YES")
            } else {
                Result.failure(RuntimeException(response.massage.toString()))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun getTrialListData(size:Int,current:Int)= liveData(Dispatchers.IO){
        val result=try {
            val response=RecommendDetailNetwork.recommendDetailService.getTrialList(size.toString(),current.toString()).await()
            if(response.code.toString()=="200"){
                Result.success(response.data?.records)
            } else {
                Result.failure(RuntimeException(response.message))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun sendMassage(content:String,userId:String,spotId:String)= liveData(Dispatchers.IO){
        val result=try {
            val response=RecommendDetailNetwork.recommendDetailService.sendTalkMassage(content,userId,spotId).await()
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

    fun sendImage(userId: String,spotId: String,file: File)= liveData(Dispatchers.IO){
        val result=try {
            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
            val part =
                MultipartBody.Part.createFormData("file", "file.jpg", requestFile)
            val response=RecommendDetailNetwork.recommendDetailService.sendTalkImage(part,spotId,userId).await()
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

}