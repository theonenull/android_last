package com.example.news.classes

import com.google.gson.JsonArray
import com.google.gson.JsonObject

data class AnnouncementNetworkBackItem(
    var code:Int,
    var Massage:String,
    var data:JsonObject,
    var total:Int,
    var size:Int,
    var current:Int,
    var orders:JsonArray,
    var optimizeCountSql:Boolean,
    var searchCount:Boolean,
    var countId:Int,
    var maxLimit:Int,
    var pages:Int
    )
{

}