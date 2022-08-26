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
import com.example.news.adapter.mainActivity.CategoryAdapter
import com.example.news.application.MyApplication
import com.example.news.utilClass.Util
import com.example.news.classes.network.categoryNetwork.Records
import com.example.news.databinding.FragmentCategoryBinding
import com.example.news.keyValue.USERDATA
import com.example.news.theActivitys.MainActivity
import com.example.news.utilClass.UserDataUtil
import com.example.news.viewModel.RecommendViewModel
import kotlinx.coroutines.*
import kotlin.properties.Delegates


class CategoryFragment(viewModel: RecommendViewModel) : Fragment() {
    private val binding: FragmentCategoryBinding get() = _binding!!
    private var _binding: FragmentCategoryBinding?=null
    private lateinit var activityOnFragment:MainActivity
    val viewModel:RecommendViewModel
    init {
        this.viewModel=viewModel
    }
    private val job= Job()
    private val scope= CoroutineScope(job)
    private var current=1
    private val size=20
    private var total by Delegates.notNull<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentCategoryBinding.inflate(layoutInflater)
        binding
        viewModel.categoryData.observe(activityOnFragment){
            val result=it.getOrNull()
            if(result!=null){
                try {
                    it.getOrThrow()
                    val list= listOf<Records>()
                    val mMutableList = list.toMutableList()
                    for(i in result.data?.records!!){
                        mMutableList.add(i)
                    }
                    total= result.data!!.total
                    initRecyclerView(mMutableList)
                    initAnim()
                    if(binding.switchWidget.isRefreshing){
                        binding.switchWidget.isRefreshing=false
                        Util.easyToast("加载成功")
                    }
                }
                catch (e:Exception){
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
        binding.switchWidget.setColorSchemeColors(MyApplication.context.getColor(R.color.red))
        binding.switchWidget.setOnRefreshListener {
            scope.launch {
                withContext(Dispatchers.IO){
                    viewModel.refreshSearchData(RecommendViewModel.SizeWithCurrent(size,current)) }
            }
        }
        initRecyclerView(listOf())
        viewModel.refreshSearchData(RecommendViewModel.SizeWithCurrent(size,current))
        binding.categoryFab.setOnClickListener{
//            initRecyclerView(listOf())
//            viewModel.refreshSearchData(RecommendViewModel.SizeWithCurrent(size,current))
            if(UserDataUtil.getIsLogin()){
                activityOnFragment.replaceFragmentToAddCateGory(UserDataUtil.getUserId(),viewModel)
            }
            else{
                Util.easyToast("未登录")
            }
        }
        return binding.root
    }
    private fun initRecyclerView(list: List<Records>){
        val layoutManager = LinearLayoutManager(activityOnFragment)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.categoryRecyclerView.layoutManager = layoutManager
        val adapter= CategoryAdapter(list,activityOnFragment)
        binding.categoryRecyclerView.adapter=adapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activityOnFragment=activity as MainActivity
    }
    private fun initAnim() {
        val animation: Animation = AnimationUtils.loadAnimation(activityOnFragment, R.anim.category_item)
        val layoutAnimationController = LayoutAnimationController(animation)
        layoutAnimationController.order = LayoutAnimationController.ORDER_NORMAL
        layoutAnimationController.delay = 0.2f
        binding.categoryRecyclerView.layoutAnimation = layoutAnimationController
    }
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}