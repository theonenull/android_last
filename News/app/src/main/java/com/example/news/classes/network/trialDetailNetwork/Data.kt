package com.example.news.classes.network.trialDetailNetwork


data class Data (
    var judgeId: Int = 0,
    var userId: Int = 0,
    var spotId: Int = 0,
    var judgeName: String? = null,
    var summary: String? = null,
    var approved: String? = null,
    var detail: String? = null,
    var image: String? = null,
    var clickAmount: Int = 0,
    var time: String? = null,
    var multiplier: Int = 0,
    var addend: Int = 0,
    var positive: Int = 0,
    var negative: Int = 0,
){

}