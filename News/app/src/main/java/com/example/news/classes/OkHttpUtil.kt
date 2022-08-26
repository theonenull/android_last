package com.example.news.classes

import android.content.Context
import com.example.news.application.MyApplication
import com.example.news.interceptor.AddCookiesInterceptor
import com.example.news.interceptor.ReceivedCookiesInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


class OkHttpUtil(context: Context, connectTimeOut: Int, writeTimeOut: Int, readTimeOut: Int) {
    var context:Context

    private val builder:OkHttpClient.Builder = OkHttpClient.Builder()
        .connectTimeout(connectTimeOut.toLong(), TimeUnit.SECONDS)
        .writeTimeout(writeTimeOut.toLong(), TimeUnit.SECONDS)
        .readTimeout(readTimeOut.toLong(), TimeUnit.SECONDS)
    val client: OkHttpClient = builder.addInterceptor(ReceivedCookiesInterceptor()).build()
    val okHttpClient = builder.addInterceptor(AddCookiesInterceptor()).build();

    init{
        this.context=context
    }
}