package com.example.news.retrofit

//import com.example.news.dataClass.RetrofitRegisterBackData

import com.example.news.classes.Root
import com.example.news.classes.network.SendMassageBackData
import com.example.news.classes.network.base.Root2
import com.example.news.viewModel.LoginViewModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface TotalRetrofit {
    @GET("/announcement/getAllAnnouncement")
    fun getAnnouncement(@Query("size")size:Int,@Query("current")current:Int):Call<Root>

    @FormUrlEncoded
    @POST("/user/login")
    fun getLoginData(@Field("username") username:String, @Field("password") password:String): Call<com.example.news.classes.network.loginNetwork.Root>

    @FormUrlEncoded
    @POST("/user/register")
    fun getRegisterData(@Field("username") username:String,@Field("password") password:String): Call<com.example.news.classes.network.recommendNetwork.Root>

//    @POST("/user/getPublicKey")
//    fun getPublicKey():Call<RetrofitRegisterBackData>

    @GET("/spot/getAllGood")
    fun getRecommend(@Query("size")size: Int,@Query("current")current: Int):Call<com.example.news.classes.network.recommendNetwork.Root>

    @GET("/category/getAllFollow")
    fun getCategory(@Query("size")size: Int,@Query("current")current: Int):Call<com.example.news.classes.network.categoryNetwork.Root>

    @GET("/spot/getGoodBySort")
    fun getCateGoryDetail(@Query("size")size: Int,@Query("current")current: Int,@Query("spotSort")spotSort:String):Call<com.example.news.classes.network.categoryDetailNetwork.Root>

    @GET("/spot/getGood")
    fun getSearchRecommendData(@Query("size")size: String,@Query("current")current: String,@Query("spotName")spotName:String):Call<com.example.news.classes.network.recommendNetwork.Root>

    @GET("/user/select")
    fun getUserDataById(@Query("userId")userId:String):Call<com.example.news.classes.network.getUserDataByIdNetwork.Root>

    @GET("/judge/getAllGood")
    fun getTrialList(@Query("size")size: String,@Query("current")current: String):Call<com.example.news.classes.network.trialListNetwork.Root>

    @GET("/conversation/insertReplyWithoutPic")
    fun sendTalkMassage(@Query("content")current: String,@Query("userId")userId:String,@Query("spotId")spotId:String):Call<SendMassageBackData>

    @Multipart
    @POST("/conversation/insertReplyWithPic")
    fun sendTalkImage(@Part file: MultipartBody.Part, @Query("spotId") spotId:String, @Query("userId") userId:String):Call<SendMassageBackData>


    @Multipart
    @POST("/user/photoUpdate")
    fun sendUserChangeImage(@Part file: MultipartBody.Part, @Query("userId") userId:String):Call<SendMassageBackData>

    @PUT("/user/baseMessageUpdate")
    fun sendUserChangeMassage(@Query("age")age: String,@Query("nickname")nickname:String,@Query("phone")phone:String,@Query("qq")qq:String,@Query("userId")userId: String):Call<SendMassageBackData>

    @GET("/judge/getGoodById")
    fun getTrialDetail(@Query("judgeId")judgeId:String):Call<com.example.news.classes.network.trialDetailNetwork.Root>

    @POST("/vote/insert")
    fun postVote(@Query("flag")flag:String,@Query("id")id:String,@Query("userId")userId:String):Call<com.example.news.classes.network.base.Root>

    @POST("/category/insert")
    fun insertNewCategory(@Query("goodSort")goodSort:String,@Query("spotDescribe")spotDescribe:String):Call<Root2>
}