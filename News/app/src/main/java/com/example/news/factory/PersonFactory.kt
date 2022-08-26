package com.example.news.factory


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.news.viewModel.PersonViewModel


class PersonFactory(private val userId:String):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PersonViewModel(userId) as T
    }
}