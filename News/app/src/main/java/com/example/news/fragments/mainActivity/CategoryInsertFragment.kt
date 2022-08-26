package com.example.news.fragments.mainActivity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.news.R
import com.example.news.databinding.FragmentCategoryInsertBinding
import com.example.news.databinding.FragmentRecommendFoundBinding
import com.example.news.theActivitys.MainActivity
import com.example.news.utilClass.Util
import com.example.news.viewModel.RecommendViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CategoryInsertFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoryInsertFragment(val userId:String,val viewModel: RecommendViewModel) : Fragment() {
    private val binding: FragmentCategoryInsertBinding get() = _binding!!
    private var _binding: FragmentCategoryInsertBinding?=null
    lateinit var activityOnFragment:MainActivity
    // TODO: Rename and change types of parameters
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentCategoryInsertBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        binding.buttonToAddNewCategory.setOnClickListener {
            if(binding.editText.text.toString()==""&&binding.editText2.text.toString()==""){
                Util.easyToast("信息不全")
            }
            else{
                viewModel.refreshNewCategory(RecommendViewModel.Category(binding.editText.text.toString(),binding.editText2.text.toString()))
            }
        }
        viewModel.addNewCategoryDataBack.observe(activityOnFragment){
            try {
                it.getOrThrow()
                if(it.getOrNull()!=null){
                    Util.easyToast("添加成功，等待审核")
                    activityOnFragment.onBackPressed()
                }
            }catch (e:Exception){
                Util.easyToast(e.message.toString())
            }
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activityOnFragment=activity as MainActivity
    }

}