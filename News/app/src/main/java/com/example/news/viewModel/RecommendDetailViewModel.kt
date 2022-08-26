package com.example.news.viewModel

import androidx.lifecycle.ViewModel
import com.example.news.utilClass.Util
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.news.classes.TalkMassage
import com.example.news.repository.RecommendDetailRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File



class RecommendDetailViewModel(spotId: String,userId: String):ViewModel() {
    private val webSocketClient:WebSocketClient
    val massageList=ArrayList<TalkMassage>()
    val massageOnTime=MutableLiveData<TalkMassage>()
    init {
        val serverURI: URI = URI.create("ws://39.107.65.181:8686/websocket/$userId/$spotId")
        webSocketClient = object : WebSocketClient(serverURI) {
            override fun onOpen(handshakedata: ServerHandshake) {
                Util.logDetail("webSocket","onOpen")
            }
            override fun onMessage(message: String) {
                massageList.add(Json.decodeFromString(message))
                massageOnTime.postValue(Json.decodeFromString(message))
                Util.logDetail("webSocket", "onMassage $message")
            }
            override fun onClose(code: Int, reason: String, remote: Boolean) {
                Util.logDetail("webSocket","onClose")
            }
            override fun onError(ex: Exception) {
                Util.logDetail("webSocket","onError")
            }
        }
        webSocketClient.connect()
    }
    //获取庭审列表
    private val trialSearchData= MutableLiveData<SizeWithCurrent>()
    fun refreshSearch(sizeWithCurrent: SizeWithCurrent){
        this.trialSearchData.postValue(sizeWithCurrent)
    }
    val trialListData=Transformations.switchMap(trialSearchData){
        RecommendDetailRepository.getTrialListData(it.size,it.current)
    }

    //发送消息
    private val massageForSend=MutableLiveData<MassageWithUserIdWithSpotId>()
    fun refreshMassageForSend(data:MassageWithUserIdWithSpotId){
        this.massageForSend.postValue(data)
    }
    val sendMassageBackData=Transformations.switchMap(massageForSend){
        RecommendDetailRepository.sendMassage(it.massage,it.userId,it.spotId)
    }

    //发送图片
    private val imageForSend=MutableLiveData<File>()
    fun refreshImageForSend(data:File){
        this.imageForSend.postValue(data)
    }
    val sendImageBackData=Transformations.switchMap(imageForSend){
        RecommendDetailRepository.sendImage(userId,spotId,it)
    }

    //获取庭审数据
    private val trialIdData=MutableLiveData<String>()
    fun refreshTrialIdData(string: String){
        this.trialIdData.postValue(string)
    }
    val trialData=Transformations.switchMap(trialIdData){
        RecommendDetailRepository.getTrailData(it)
    }
    //投票
    private val vote=MutableLiveData<FlagWithId>()
    fun refreshVote(flag:FlagWithId){
        this.vote.postValue(flag)
    }
    val voteDataBack=Transformations.switchMap(vote){
        RecommendDetailRepository.postVote(it.flag,userId,it.id)
    }


    override fun onCleared() {
        webSocketClient.close()
    }
    class SizeWithCurrent(var size:Int,var current:Int)
    class MassageWithUserIdWithSpotId(var massage:String,var userId:String,var spotId:String){}
    class FlagWithId(val flag: String,val id:String){}
}