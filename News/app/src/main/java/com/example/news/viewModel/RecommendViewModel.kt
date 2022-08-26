package com.example.news.viewModel

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.news.repository.RecommendRepository

class RecommendViewModel:ViewModel(){
    //获取推荐
    private val recommendListData=MutableLiveData<SizeWithCurrent>()
    fun refreshRecommendListData(data:SizeWithCurrent){
        recommendListData.postValue(data)
    }
    val recommendList=Transformations.switchMap(recommendListData){
        RecommendRepository.getRecommend(it.size,it.current)
    }

    //获取类别
    private val searchCategoryData=MutableLiveData<SizeWithCurrent>()
    fun refreshSearchData(name:SizeWithCurrent){
        this.searchCategoryData.postValue(name)
    }
    val categoryData=Transformations.switchMap(searchCategoryData){
        RecommendRepository.getCategory(it.size,it.current)
    }

    //获取类别详情
    private val searchCategoryDetailData=MutableLiveData<SizeWithCurrentWithSpotSort>()
    fun refreshCategoryDetailData(name:SizeWithCurrentWithSpotSort){
        this.searchCategoryDetailData.postValue(name)
    }
    val categoryDetailData=Transformations.switchMap(searchCategoryDetailData){
        RecommendRepository.getCategoryDetail(it.size,it.current,it.SpotSort)
    }

    //关键词搜索热点搜索
    private val searchRecommendKey=MutableLiveData<SizeWithCurrentWithSpotSort>()
    fun refreshSearchRecommendKey(key:SizeWithCurrentWithSpotSort){
        this.searchRecommendKey.postValue(key)
    }
    val searchRecommendData=Transformations.switchMap(searchRecommendKey){
        RecommendRepository.getSearchRecommendData(it.size.toString(),it.current.toString(),it.SpotSort)
    }
    //添加类别
    private val newCategory=MutableLiveData<Category>()
    fun refreshNewCategory(category:Category){
        this.newCategory.postValue(category)
    }
    val addNewCategoryDataBack=Transformations.switchMap(newCategory){
        RecommendRepository.addNewCateGory(it.goodSort,it.spotDescribe)
    }


    private val userData=MutableLiveData<String>()
    fun refreshUserData(){
        this.userData.postValue(userData.value)
    }
    val userDataForMainActivity=Transformations.switchMap(userData){
        RecommendRepository.getUserData()
    }
    class SizeWithCurrent(var size:Int,var current:Int){}
    class SizeWithCurrentWithSpotSort(var size:Int,var current:Int,var SpotSort:String){}
    class Category(val goodSort:String,val spotDescribe:String){}
    //判断是否联网

}