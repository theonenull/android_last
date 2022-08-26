package com.example.news.classes

data class Data(
    var records: List<Records>,
    var total:Int,
    var size:Int,
    var current:Int,
    var orders: List<String>,
    var optimizeCountSql:Boolean,
    var searchCount:Boolean,
    var countId: String,
    var maxLimit: String,
    var pages:Int
){

}