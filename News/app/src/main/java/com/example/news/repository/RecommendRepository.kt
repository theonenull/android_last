package com.example.news.repository

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.liveData
import com.example.news.application.MyApplication
import com.example.news.utilClass.Util
import com.example.news.utilClass.Util.await
import com.example.news.classes.network.getUserDataByIdNetwork.User
import com.example.news.keyValue.LOGIXDATA
import com.example.news.keyValue.USERDATA
import com.example.news.network.LoginNetwork
import com.example.news.network.RecommendNetWork
import kotlinx.coroutines.Dispatchers


object RecommendRepository {
    fun getRecommend(size:Int,current: Int)= liveData(Dispatchers.IO){
        val result=try {
                val result=LoginNetwork.loginService.getRecommend(size,current).await()
                Result.success(result)
        }catch (e:Exception){
            Util.logDetail("sssssssssss",e.toString())
            Result.failure(e)
        }
        emit(result)
    }
    fun addNewCateGory(goodSort:String,spotDescribe:String)= liveData(Dispatchers.IO){
        val result=try {
            val result=LoginNetwork.loginService.insertNewCategory(goodSort,spotDescribe).await()
            if(result.code.toString()=="200"){
                Result.success(result)
            }
            else{
                Result.failure(RuntimeException(result.massage.toString()))
            }
        }catch (e:Exception){
            Util.logDetail("sssssssssss",e.toString())
            Result.failure(e)
        }
        emit(result)
    }
    fun getCategory(size: Int, current:Int)= liveData(Dispatchers.IO){
        val result=try {
            val result=RecommendNetWork.recommendService.getCategory(size,current).await()
            Result.success(result)
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun getCategoryDetail(size: Int, current:Int,spotSort:String)= liveData(Dispatchers.IO){
        val result=try {
            val result=RecommendNetWork.recommendService.getCateGoryDetail(size,current,spotSort).await()
            Result.success(result)
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun getSearchRecommendData(size: String,current:String,spotSort: String)= liveData(Dispatchers.IO){
        val result=try {
            val result=RecommendNetWork.recommendService.getSearchRecommendData(size,current,spotSort).await()
            Result.success(result)
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun getUserData()= liveData(Dispatchers.IO){
        if(Util.getSharePreferencesString(null,LOGIXDATA.PLACE,LOGIXDATA.ISLOGIN)=="true"){
            val result=try {
                if(checkConnectNetwork(MyApplication.context)){
                    val result=RecommendNetWork.recommendService.getUserDataById(Util.getSharePreferencesString(null,USERDATA.PLACE,USERDATA.USER_ID)).await()
                    Result.success(result.data?.user)
//                    val dir:String= Environment.getExternalStorageDirectory().absolutePath + "/user/image"
//                    try {
//                        val bitmap: Bitmap = Glide.with(context)
//                            .load<Any>("url")
//                            .asBitmap()
//                            .into(1, 1)
//                            .get()
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
//                            out.flush()
////                        File file = new File(dir + fileName + ".jpg");
////                        FileOutputStream out = new java.io.FileOutputStream(file);
////                        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                        out.flush();
//                        out.close();
//                            out.close()
//                    } catch (e: java.lang.Exception) {
//                        e.printStackTrace()
//                    }
                }
                else {
                    val result=User(
                        Util.getSharePreferencesString(null,USERDATA.PLACE,USERDATA.USER_ID).toInt(),
                        Util.getSharePreferencesString(null,USERDATA.PLACE,USERDATA.ROLE),
                        Util.getSharePreferencesString(null,USERDATA.PLACE,""),
                        Util.getSharePreferencesString(null,USERDATA.PLACE,""),
                        Util.getSharePreferencesString(null,USERDATA.PLACE,USERDATA.NICKNAME),
                        Util.getSharePreferencesString(null,USERDATA.PLACE,USERDATA.SALT),
                        Util.getSharePreferencesString(null,USERDATA.PLACE,USERDATA.IMAGE),
                        Util.getSharePreferencesString(null,USERDATA.PLACE,USERDATA.AGE),
                    )
                    Result.success(result)
                }
            }catch (e:Exception){
                Result.failure(e)
            }
            emit(result)
        }
        else{
            emit(null)
        }
    }
    private fun checkConnectNetwork(context: Context): Boolean {
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val net = conn.activeNetworkInfo
        return net != null && net.isConnected
    }

}