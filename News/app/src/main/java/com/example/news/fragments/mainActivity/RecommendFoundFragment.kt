package com.example.news.fragments.mainActivity

//import com.example.news.adapter.mainActivity.AnnouncementListAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.adapter.mainActivity.RecommendFragmentRecyclerViewAdapter
import com.example.news.utilClass.Util.easyToast
import com.example.news.utilClass.Util.initRecyclerView
import com.example.news.databinding.FragmentRecommendFoundBinding
import com.example.news.theActivitys.MainActivity
import com.example.news.viewModel.RecommendViewModel


class RecommendFoundFragment(viewModel:RecommendViewModel) : Fragment() {
    private val binding:FragmentRecommendFoundBinding  get() = _binding!!
    private var _binding: FragmentRecommendFoundBinding?=null
    private lateinit var activityInFragment:MainActivity
    private var viewModel: RecommendViewModel
    val size=20
    val current=1
    init {
        this.viewModel=viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentRecommendFoundBinding.inflate(layoutInflater)
        viewModel.searchRecommendData.observe(activityInFragment){
            val result=it.getOrNull()
            if (result != null) {
                if(result.data?.records !=null){
                    try {
                        it.getOrThrow()
                        binding.recommendSearchRecyclerView.initRecyclerView(activityInFragment,LinearLayoutManager.VERTICAL,
                            RecommendFragmentRecyclerViewAdapter(result.data!!.records!!,activityInFragment))
                        easyToast("加载成功")
                    }catch (e:Exception){
                        easyToast(e.message.toString())
                    }
                }
                else{
                    easyToast("无结果")
                }
            }

        }
        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query==""){
                    easyToast("不得为空")
                }
                query?.let {
                    RecommendViewModel.SizeWithCurrentWithSpotSort(size,current,it)
                }?.let { viewModel.refreshSearchRecommendKey(it) }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activityInFragment=this.activity as MainActivity
    }

//    fun initRecyclerView(){
//        val layoutManager = LinearLayoutManager(activityInFragment)
//        layoutManager.orientation = LinearLayoutManager.VERTICAL
//        binding.categoryRecyclerView.layoutManager = layoutManager
//        val beanAdapter = this.activity?.let {
//            AnnouncementListAdapter(announcementListData, it,this, this.activity as AnnouncementActivity,)
//        }
//        binding.categoryRecyclerView.adapter = beanAdapter
//    }
}