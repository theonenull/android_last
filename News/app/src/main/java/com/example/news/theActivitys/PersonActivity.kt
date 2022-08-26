package com.example.news.theActivitys
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.example.news.R
import com.example.news.utilClass.Util
import com.example.news.databinding.ActivityPersonBinding
import com.example.news.factory.PersonFactory
import com.example.news.fragments.personActivity.PersonChangeDataFragment
import com.example.news.fragments.personActivity.PersonFirstPageFragment
import com.example.news.keyValue.USERDATA
import com.example.news.viewModel.PersonViewModel

class PersonActivity : AppCompatActivity() {
    private val binding:ActivityPersonBinding get() = _binding!!
    private var _binding:ActivityPersonBinding? = null
    private lateinit var viewModel:PersonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= ViewModelProvider(this, PersonFactory(Util.getSharePreferencesString(this,USERDATA.PLACE,USERDATA.USER_ID))).get(PersonViewModel::class.java)
        _binding=ActivityPersonBinding.inflate(layoutInflater)
        Util.repLaceFragment(PersonFirstPageFragment(viewModel),false,this,R.id.fragment)
        windowSetting()
        setContentView(binding.root)
    }
    fun replaceFragmentToPersonChangeDataFragment(){
        Util.repLaceFragment(PersonChangeDataFragment(this.viewModel),true,this,R.id.fragment)
    }
    private fun windowSetting(){
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.statusBarColor = resources.getColor(R.color.purple_200);
    }
}