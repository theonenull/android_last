package com.example.news.theActivitys

import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.news.R
import com.example.news.utilClass.Util
import com.example.news.databinding.ActivityNewsDetailBinding
import com.example.news.factory.NewsDetailFactory
import com.example.news.fragments.newsDetailActivity.*
import com.example.news.keyValue.USERDATA
import com.example.news.utilClass.UserDataUtil
import com.example.news.viewModel.RecommendDetailViewModel

class NewsDetailActivity: AppCompatActivity() {
    private val binding:ActivityNewsDetailBinding get() = _binding!!
    private var _binding:ActivityNewsDetailBinding ? = null
    lateinit var spotId: String
    private lateinit var viewModel:RecommendDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        windowSetting()
        spotId= intent.getStringExtra("spotId").toString()
        viewModel= ViewModelProvider(this,NewsDetailFactory(Util.getSharePreferencesString(null,USERDATA.PLACE,USERDATA.USER_ID),spotId)).get(RecommendDetailViewModel::class.java)
        binding.changeList.setLength((Util.getScreenWidth(this)))
        Util.action(binding.changeList.getLineSpace(),"middle",binding.changeList.getLineSpace().getMiddle(),binding.changeList.getLength()/6,300, AccelerateDecelerateInterpolator())
        Util.repLaceFragment(NewsDetailFragment(),false,this, R.id.fragmentDetail)

        binding.changeList.getButtonTwo().setOnClickListener {
            Util.action(binding.changeList.getLineSpace(),"middle",binding.changeList.getLineSpace().getMiddle(),binding.changeList.getLength()/2,300, AccelerateDecelerateInterpolator())
            Util.repLaceFragment(TalkFragment(viewModel,spotId.toString()),false,this,R.id.fragmentDetail)
        }
        binding.changeList.getButtonThree().setOnClickListener {
            Util.action(binding.changeList.getLineSpace(),"middle",binding.changeList.getLineSpace().getMiddle(),binding.changeList.getLength()/6*5,300, AccelerateDecelerateInterpolator())
            Util.repLaceFragment(TrialListFragment(viewModel),false,this, R.id.fragmentDetail)
        }
        binding.changeList.getButtonOne().setOnClickListener {
            Util.action(binding.changeList.getLineSpace(),"middle",binding.changeList.getLineSpace().getMiddle(),binding.changeList.getLength()/6,300, AccelerateDecelerateInterpolator())
            Util.repLaceFragment(NewsDetailFragment(),false,this, R.id.fragmentDetail)
        }
    }
    private fun windowSetting(){
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.statusBarColor = resources.getColor(R.color.purple_200);
    }
    fun replaceFragmentToTrialDetail(userId:String,judgeId:String){
        Util.repLaceFragment(TrialDetailFragment(this.viewModel,userId,spotId,judgeId),false,this, R.id.fragmentDetail)
    }
}