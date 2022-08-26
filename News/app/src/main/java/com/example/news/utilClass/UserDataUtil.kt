package com.example.news.utilClass

import com.example.news.keyValue.LOGIXDATA
import com.example.news.keyValue.USERDATA

object UserDataUtil {
    fun getIsLogin():Boolean{
        if(Util.getSharePreferencesString(null,LOGIXDATA.PLACE,LOGIXDATA.ISLOGIN)=="true"){
            return true
        }
        return false
    }
    fun getUserId(): String {
        return Util.getSharePreferencesString(null,USERDATA.PLACE,USERDATA.USER_ID)
    }
    fun getUserRole(): String {
        return Util.getSharePreferencesString(null,USERDATA.PLACE,USERDATA.ROLE)
    }
    fun getSaltUserId(): String {
        return Util.getSharePreferencesString(null,USERDATA.PLACE,USERDATA.SALT)
    }
    fun getUserAge(): String {
        return Util.getSharePreferencesString(null,USERDATA.PLACE,USERDATA.AGE)
    }
    fun getNickname(): String {
        return Util.getSharePreferencesString(null,USERDATA.PLACE,USERDATA.NICKNAME)
    }
    fun getUserImage(): String {
        return Util.getSharePreferencesString(null,USERDATA.PLACE,USERDATA.IMAGE)
    }
}