package com.example.news.classes.network.loginNetwork

data class Token (
    var username: String? = null,
    var password: String? = null,
    var rememberMe: Boolean = false,
    var host: String? = null,
    var principal: String? = null,
    var credentials: String? = null,
    )
{

}