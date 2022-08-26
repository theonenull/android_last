package com.example.news.fragments.mainActivity

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.adapter.mainActivity.CategoryDetailAdapter
import com.example.news.application.MyApplication
import com.example.news.utilClass.Util
import com.example.news.classes.network.categoryDetailNetwork.Records
import com.example.news.databinding.FragmentCategoryDetailBinding
import com.example.news.theActivitys.MainActivity
import com.example.news.viewModel.RecommendViewModel
import kotlinx.coroutines.*


class CategoryDetailFragment(viewModel: RecommendViewModel, private val spotSort:String, private val description:String): Fragment() {
    private val binding: FragmentCategoryDetailBinding get() = _binding!!
    private var _binding: FragmentCategoryDetailBinding?=null
    private lateinit var activityOnFragment: MainActivity
    private var slideBoolean=true
    private var y:Float=0f
    private val viewModel:RecommendViewModel
    private var size=20
    private var current=1
    private val job= Job()
    private val scope= CoroutineScope(job)
    init {
        this.viewModel=viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentCategoryDetailBinding.inflate(layoutInflater)
        binding.spotSort.text=spotSort
        binding.sportDescribe.text=description
        y=binding.mainLinearLayout.y
        binding.imageViewToSlide.setOnClickListener {
            if(slideBoolean){
                it.isEnabled=false
                val y=binding.mainLinearLayout.y
                val animator: ObjectAnimator = ObjectAnimator.ofFloat(binding.mainLinearLayout, "Y", y,y-500f)
                animator.duration = 500
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.start()
                slideBoolean=false
                it.setBackgroundResource(R.drawable.pull)
                it.isEnabled=true
            }else{
                it.isEnabled=false
                binding.textParent.visibility=View.VISIBLE
                val y=binding.mainLinearLayout.y
                val animator: ObjectAnimator = ObjectAnimator.ofFloat(binding.mainLinearLayout, "Y", y, y+500f)
                animator.duration = 500
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.start()
                slideBoolean=true
                it.setBackgroundResource(R.drawable.push)
                it.isEnabled=true
            }
        }
        viewModel.categoryDetailData.observe(activityOnFragment){
            val result=it.getOrNull()
            if(result!=null){
                try {
                    it.getOrThrow()
                    val list= listOf<Records>()
                    val mMutableList = list.toMutableList()
                    for(i in result.data?.records!!){
                        mMutableList.add(i)
                    }
//                    total= result.data!!.total
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
        initRecyclerView(listOf())
        binding.switchWidget.setColorSchemeColors(MyApplication.context.getColor(R.color.red))
        binding.switchWidget.setOnRefreshListener {
            scope.launch {
                withContext(Dispatchers.IO){
                    viewModel.refreshCategoryDetailData(RecommendViewModel.SizeWithCurrentWithSpotSort(size,current,spotSort)) }
            }
        }
        viewModel.refreshCategoryDetailData(RecommendViewModel.SizeWithCurrentWithSpotSort(size,current,spotSort))
        return binding.root
    }

    private fun initRecyclerView(list: List<Records>){
        val layoutManager = LinearLayoutManager(activityOnFragment)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.categoryDetailRecyclerView.layoutManager = layoutManager
        val adapter= CategoryDetailAdapter(list,activityOnFragment)
        binding.categoryDetailRecyclerView.adapter=adapter
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
        binding.categoryDetailRecyclerView.layoutAnimation = layoutAnimationController
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}