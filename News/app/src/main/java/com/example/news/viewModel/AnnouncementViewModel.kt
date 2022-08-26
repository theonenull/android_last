package com.example.news.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.classes.*
import com.example.news.utilClass.Util
import com.example.news.retrofit.TotalRetrofit
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AnnouncementViewModel: ViewModel() {
    val totalData:LiveData<Root> get() = _totalData
    private val _totalData=MutableLiveData<Root>()
    val statue:LiveData<String> get() = _statue
    private val _statue=MutableLiveData<String>()

    init {
        _statue.value=""
        val job=Job()
        val scope= CoroutineScope(job)
        scope.launch {
            getData()
        }
    }
    private suspend fun <T>Call<T>.await():T{
        return suspendCoroutine {
            enqueue(object :Callback<T>{
                override fun onFailure(call: Call<T>, t: Throwable) {
                     it.resumeWithException(t)
                }
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body=response.body()
                    if (body!=null)it.resume(body)
                    else it.resumeWithException(RuntimeException("response is null"))
                }
            })
        }
    }
    private suspend fun getData(){
        withContext(Dispatchers.IO){
            try {
                val data=ServiceCreator.create<TotalRetrofit>().getAnnouncement(20,0).await()
                _totalData.postValue(data)
                Util.logDetail("++++++++++",data.toString())
            }
            catch (e:Exception){
                _statue.postValue(ProperNounClass.NETWORK_FAIL)
                Util.logDetail("SSSSSSS",e.toString())
            }
        }
    }
}