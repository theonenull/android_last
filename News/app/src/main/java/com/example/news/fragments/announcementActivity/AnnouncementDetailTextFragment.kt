package com.example.news.fragments.announcementActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.news.R
import com.example.news.databinding.FragmentAnnouncementDetailTextBinding


class AnnouncementDetailTextFragment : Fragment() {
    private val binding: FragmentAnnouncementDetailTextBinding get() = _binding!!
    private var _binding: FragmentAnnouncementDetailTextBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnnouncementDetailTextBinding.inflate(layoutInflater)
        binding.announcementTopic.text= arguments?.getString("topic")
        binding.announcementTime.text= arguments?.getString("time")
        binding.announcementContext.text= arguments?.getString("context")
        return binding.root
    }
}