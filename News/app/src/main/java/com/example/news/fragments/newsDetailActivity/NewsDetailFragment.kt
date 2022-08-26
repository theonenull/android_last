package com.example.news.fragments.newsDetailActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.classes.RecommendItem
import com.example.news.databinding.FragmentNewsDetailBinding

/**
 * A simple [Fragment] subclass.
 * Use the [NewsDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsDetailFragment : Fragment() {
    val binding:FragmentNewsDetailBinding get() = _binding
    private lateinit var _binding:FragmentNewsDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentNewsDetailBinding.inflate(layoutInflater)
        val layoutInflater=LinearLayoutManager(this.activity)
        layoutInflater.orientation=LinearLayoutManager.VERTICAL
        binding.newsDetailRecyclerView.layoutManager=layoutInflater
        val list= listOf<RecommendItem>()
        val mMutableList = list.toMutableList()
        repeat(20){
            mMutableList.add(RecommendItem("id","title","text"))
        }
//        val adapter= this.activity?.let { RecommendFragmentRecyclerViewAdapter(mMutableList, it) }
//        binding.newsDetailRecyclerView.adapter=adapter
        return binding.root
    }

}