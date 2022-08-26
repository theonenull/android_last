package com.example.news.fragments.personActivity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.news.adapter.personActivity.PersonMainPageButtonAdapter
import com.example.news.adapter.personActivity.PersonMainPageMassageAdapter
import com.example.news.utilClass.Util
import com.example.news.utilClass.Util.initRecyclerView
import com.example.news.databinding.FragmentPersonMainPaceBinding
import com.example.news.keyValue.USERDATA
import com.example.news.theActivitys.PersonActivity
import com.example.news.viewModel.PersonViewModel


class PersonFirstPageFragment(viewModel: PersonViewModel) : Fragment() {
    val binding: FragmentPersonMainPaceBinding get() = _binding
    private lateinit var _binding: FragmentPersonMainPaceBinding
    private lateinit var activityOnFragment:PersonActivity
    private val viewModel: PersonViewModel
    init {
        this.viewModel=viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentPersonMainPaceBinding.inflate(layoutInflater)
        viewModel.userData.observe(activityOnFragment){
            try {
                val result = it.getOrNull()
                if (result != null) {
                    binding.massageOnPersonMainPage.initRecyclerView(activityOnFragment,LinearLayoutManager.VERTICAL,
                        PersonMainPageMassageAdapter(Util.PersonMassage(result),activityOnFragment))
                    Glide.with(activityOnFragment).load(result.image).into(binding.iconImage)
                    Util.setSharePreferences(null,USERDATA.PLACE,null, mapOf(
                        USERDATA.IMAGE to result.image.toString(),
                        USERDATA.NICKNAME to result.nickname.toString(),
                        USERDATA.SALT to result.salt.toString(),
                        USERDATA.AGE to result.age.toString(),
                    ))
                    binding.userNickname.text= result.nickname
                }
            }
            catch (e:Exception){
                val result = it.getOrNull()
                if (result != null) {
                    binding.massageOnPersonMainPage.initRecyclerView(activityOnFragment,LinearLayoutManager.VERTICAL,
                        PersonMainPageMassageAdapter(Util.PersonMassage(result),activityOnFragment))
                    Glide.with(activityOnFragment).load(result.image).into(binding.iconImage)
                    binding.userNickname.text= result.nickname
                }
            }
        }
        viewModel.refreshUserId(Util.getSharePreferencesString(null,USERDATA.PLACE,USERDATA.USER_ID))
        binding.buttonOnPersonMainPage.initRecyclerView(activityOnFragment,
            LinearLayoutManager.HORIZONTAL,PersonMainPageButtonAdapter(Util.personMainPageButtonList,activityOnFragment))
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityOnFragment=activity as PersonActivity
    }
}