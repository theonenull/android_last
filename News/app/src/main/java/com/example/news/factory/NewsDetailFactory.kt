package com.example.news.factory


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.news.viewModel.RecommendDetailViewModel


class NewsDetailFactory(private val userId:String,private val spotId: String):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecommendDetailViewModel(spotId,userId) as T
    }
}