package com.example.news.classes.network.getUserDataByIdNetwork


data class User(
    var userId: Int = 0,
    var role: String? = null,
    var username: String? = null,
    var password: String? = null,
    var nickname: String? = null,
    var salt: String? = null,
    var image: String? = null,
    var age: String? = null,
) {

}