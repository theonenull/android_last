package com.example.news.classes.network.categoryDetailNetwork


data class Data(var records: List<Records>? = null,
                var total: Int = 0,
                var size: Int = 0,
                var current: Int = 0,
                var orders: List<String>? = null,
                var optimizeCountSql: Boolean = false,
                var searchCount: Boolean = false,
                var countId: String? = null,
                var maxLimit: String? = null,
                var pages: Int = 0,
) {

}