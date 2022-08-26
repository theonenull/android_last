package com.example.news.classes.network.detailNetwork

data class Data (
    var spotId: Int = 0,
    var userId: Int = 0,
    var categoryId: Int = 0,
    var spotName: String? = null,
    var summary: String? = null,
    var approved: String? = null,
    var detail: String? = null,
    var image: String? = null,
    var clickAmount: Int = 0,
    var time: String? = null,
    var spotSort: String? = null,
    var multiplier: Int = 0,
    var addend: Int = 0,
){}
