package com.example.news.fragments.mainActivity


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.adapter.mainActivity.RecommendFragmentRecyclerViewAdapter
import com.example.news.application.MyApplication
import com.example.news.classes.RecommendItem
import com.example.news.utilClass.Util
import com.example.news.classes.network.recommendNetwork.Records
import com.example.news.databinding.FragmentRecommendBinding
import com.example.news.theActivitys.MainActivity
import com.example.news.viewModel.RecommendViewModel
import kotlinx.coroutines.*


class RecommendFragment(viewModel: RecommendViewModel) : Fragment() {
    private var beanList=ArrayList<RecommendItem>()
    private var _binding: FragmentRecommendBinding? = null
    private val binding: FragmentRecommendBinding get() = _binding!!
    private val viewModel:RecommendViewModel by lazy { viewModel }
    private val job=Job()
    private var current=0
    private var size=20
    private lateinit var activityOnFragment:MainActivity
    private val scope= CoroutineScope(job)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentRecommendBinding.inflate(layoutInflater)
        viewModel.recommendList.observe(activityOnFragment){
            val result=it.getOrNull()
            if (result!==null){
                try {
                    it.getOrThrow()
                    val list= listOf<Records>()
                    val mMutableList = list.toMutableList()
                    for (i in result.data?.records!!){
                        mMutableList.add(i)
                    }
                    initAnim()
                    initRecyclerView(mMutableList)
                    if(binding.switchWidget.isRefreshing){
                        binding.switchWidget.isRefreshing=false
                        Util.easyToast("加载成功")
                    }
                }catch (e:Exception){
                    Util.easyToast(e.message.toString())
                }
            }
            else{
                if(binding.switchWidget.isRefreshing){
                    Util.easyToast("刷新失败")
                    binding.switchWidget.isRefreshing=false
                }
                else{
                    Util.easyToast("加载出错")
                }

            }
        }
        binding.recommendFab.setOnClickListener {
            viewModel.refreshRecommendListData(RecommendViewModel.SizeWithCurrent(size,current))
        }
        viewModel.refreshRecommendListData(RecommendViewModel.SizeWithCurrent(size,current))
        binding.switchWidget.setColorSchemeColors(MyApplication.context.getColor(R.color.red))
        binding.switchWidget.setOnRefreshListener {
            scope.launch {
                    withContext(Dispatchers.IO){
                        viewModel.refreshRecommendListData(RecommendViewModel.SizeWithCurrent(size,current)) }
            }

        }
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView(list: List<Records>){
        val layoutManager = LinearLayoutManager(activityOnFragment.baseContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recommendRecyclerView.layoutManager = layoutManager
        val adapter= RecommendFragmentRecyclerViewAdapter(list, activityOnFragment)
        binding.recommendRecyclerView.adapter=adapter
    }
    private fun initAnim() {
        val animation: Animation = AnimationUtils.loadAnimation(activity, R.anim.my_anim)
        val layoutAnimationController = LayoutAnimationController(animation)
        layoutAnimationController.order = LayoutAnimationController.ORDER_NORMAL
        layoutAnimationController.delay = 0.2f
        binding.recommendRecyclerView.layoutAnimation = layoutAnimationController
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityOnFragment= activity as MainActivity
    }

}