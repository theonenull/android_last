package com.example.news.interceptor

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.news.application.MyApplication

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


internal class ReceivedCookiesInterceptor() : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        //这里获取请求返回的cookie
        Log.d("theresponse",originalResponse.body().toString())
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies: HashSet<String> = HashSet()
            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }
            //保存的sharepreference文件名为cookieData
            val sharedPreferences: SharedPreferences =
                MyApplication.context.getSharedPreferences("cookieData", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putStringSet("cookie", cookies)
            editor.apply()
            Log.d("blblbllblblblbl",cookies.toString())
            Log.d("blblbllblblblbl",originalResponse.body().toString())
        }
        return originalResponse
    }
}