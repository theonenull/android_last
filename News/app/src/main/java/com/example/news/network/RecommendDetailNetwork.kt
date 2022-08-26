package com.example.news.network

import com.example.news.application.MyApplication
import com.example.news.classes.OkHttpUtil
import com.example.news.retrofit.TotalRetrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RecommendDetailNetwork {
    private const val BASE_URL="http://39.107.65.181:8686/"
    private val retrofit= Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpUtil(MyApplication.context,1000,1000,1000).client)
        .build()
    val recommendDetailService: TotalRetrofit = retrofit.create(TotalRetrofit::class.java)
}