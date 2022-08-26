package com.example.news.interceptor

import android.content.Context
import com.example.news.application.MyApplication
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


internal class AddCookiesInterceptor : Interceptor {
    var context: Context = MyApplication.context
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val preferences: HashSet<String> =
            context.getSharedPreferences("cookieData", Context.MODE_PRIVATE)
                .getStringSet("cookie", null) as HashSet<String>
        for (cookie in preferences) {
            builder.addHeader("Cookie", cookie)
        }
        return chain.proceed(builder.build())
    }

}