package com.example.news.fragments.newsDetailActivity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.adapter.recommendDetailActivity.TrialListAdapter
import com.example.news.utilClass.Util
import com.example.news.utilClass.Util.initRecyclerView
import com.example.news.databinding.FragmentTrialListBinding
import com.example.news.theActivitys.NewsDetailActivity
import com.example.news.viewModel.RecommendDetailViewModel

class TrialListFragment(viewModel: RecommendDetailViewModel) : Fragment() {
    private val binding:FragmentTrialListBinding get() = _binding
    private lateinit var _binding:FragmentTrialListBinding
    private val viewModel:RecommendDetailViewModel
    private lateinit var activityOnFragment:NewsDetailActivity
    init {
        this.viewModel=viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentTrialListBinding.inflate(layoutInflater)
//        binding.
        viewModel.trialListData.observe(activityOnFragment){
            val result=it.getOrNull()
            if(result!=null){
                try {
                    it.getOrThrow()
                    binding.trialListRecyclerView.initRecyclerView(activityOnFragment,LinearLayoutManager.VERTICAL,TrialListAdapter(result,activityOnFragment))
                }
                catch (e:Exception){
                    Util.easyToast(e.message.toString())
                }
            }
        }
        // Inflate the layout for this fragment
        viewModel.refreshSearch(RecommendDetailViewModel.SizeWithCurrent(20,1))
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityOnFragment=activity as NewsDetailActivity
    }
}