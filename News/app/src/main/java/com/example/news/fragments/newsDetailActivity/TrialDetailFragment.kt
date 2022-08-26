package com.example.news.fragments.newsDetailActivity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.news.databinding.FragmentTrialDetailBinding
import com.example.news.keyValue.FRAG
import com.example.news.theActivitys.NewsDetailActivity
import com.example.news.utilClass.Util
import com.example.news.viewModel.RecommendDetailViewModel
import kotlinx.coroutines.*


class TrialDetailFragment(val viewModel:RecommendDetailViewModel, val userId:String, val spotId:String,
                          private val judgeId:String) : Fragment() {
    private val binding:FragmentTrialDetailBinding get() = _binding
    private lateinit var _binding:FragmentTrialDetailBinding
    lateinit var activityOnFragment:NewsDetailActivity
    val job=Job()
    val scope= CoroutineScope(job)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentTrialDetailBinding.inflate(layoutInflater)
        binding.mainCompetitionText
        viewModel.trialData.observe(activityOnFragment){
            try {
                it.getOrThrow()
                val result=it.getOrNull()
                if (result != null) {
                    binding.mainCompetitionText.setLineData(result.positive.toFloat(),result.negative.toFloat())
                    binding.mainCompetitionText.setSupportText("反方 ${result.negative}")
                    binding.mainCompetitionText.setOpposition("正方 ${result.positive}")
                    binding.trialQuestion.text = result.summary
                }
                else{
                    Util.easyToast("ssssssssss")
                }
            }
            catch (e:Exception){
                Util.easyToast(e.message.toString())
            }
        }
        binding.mainCompetitionText.getSupportText().setOnClickListener {
            Util.showInformation("确定投票给正方吗",this.activityOnFragment) {
                viewModel.refreshVote(RecommendDetailViewModel.FlagWithId(FRAG.FRAG_SUPPORT,judgeId))
            }
        }
        binding.mainCompetitionText.getOppositionText().setOnClickListener {
            Util.showInformation("确定投票给反方吗",this.activityOnFragment) {
                viewModel.refreshVote(RecommendDetailViewModel.FlagWithId(FRAG.FRAG_OPPOSITION,judgeId))
            }
        }
        viewModel.voteDataBack.observe(activityOnFragment){ it ->
            try {
                it.getOrThrow()
                val result=it.getOrNull()
                result?.let {
                    if(it=="YES"){
                        Util.easyToast("投票成功")
                    }
                }
            }catch (e:Exception){
                Util.easyToast("投票失败，您已投过")
                Util.logDetail("投票错误",e.message.toString())
            }
        }
        scope.launch {
            withContext(Dispatchers.IO) {
                while (true){
                    delay(2000)
                    viewModel.refreshTrialIdData(judgeId)
                }
            }
        }
        viewModel.refreshTrialIdData(judgeId)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityOnFragment= activity as NewsDetailActivity
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}