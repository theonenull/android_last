package com.example.news.fragments.announcementActivity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.adapter.AnnouncementListAdapter

import com.example.news.classes.Records
import com.example.news.utilClass.Util
import com.example.news.databinding.FragmentAnnouncementListBinding
import com.example.news.theActivitys.AnnouncementActivity


class AnnouncementListFragment : Fragment() {
    private val binding:FragmentAnnouncementListBinding get() = _binding!!
    private var _binding:FragmentAnnouncementListBinding?=null
    private var announcementListData: List<Records> = listOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding=FragmentAnnouncementListBinding.inflate(layoutInflater)
        val bundle = arguments
        activity?.baseContext?.let { initRecyclerView(it) }
        return binding.root
    }
    private fun initRecyclerView(context: Context){
        val layoutManager = LinearLayoutManager(activity?.baseContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.announcementList.layoutManager = layoutManager
        val beanAdapter = this.activity?.let {
            AnnouncementListAdapter(announcementListData, it,this, this.activity as AnnouncementActivity,)
        }
        binding.announcementList.adapter = beanAdapter
    }
    fun setAnnouncementListData(list: List<Records>){
        try {
            this.announcementListData=list
            activity?.baseContext?.let { initRecyclerView(it) }
        }catch (e:Exception){
            Util.logDetail("Exceeption",e.toString())
        }

    }

}