package com.example.news.theActivitys

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.news.R
import com.example.news.classes.ProperNounClass
import com.example.news.utilClass.Util
import com.example.news.databinding.ActivityAnnouncementBinding
import com.example.news.fragments.announcementActivity.AnnouncementDetailTextFragment
import com.example.news.fragments.announcementActivity.AnnouncementListFragment
import com.example.news.viewModel.AnnouncementViewModel

class AnnouncementActivity : AppCompatActivity() {
    lateinit var viewModel: AnnouncementViewModel
    private val binding:ActivityAnnouncementBinding  get() =_binding!!
    private var _binding:ActivityAnnouncementBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityAnnouncementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel=ViewModelProvider(this).get(AnnouncementViewModel::class.java)
        viewModel.totalData.observe(this){
            Util.logDetail("ActivityOne",it?.data?.records.toString())
            val tempFragment= AnnouncementListFragment()
            Util.repLaceFragment(tempFragment,false,this, R.id.announcementFragment)
            tempFragment.setAnnouncementListData(it.data.records)
        }
        viewModel.totalData.value.toString()
        Util.logDetail("ActivityTwo", viewModel.totalData.value?.data?.records.toString())
        val toolbar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewModel.statue.observe(this){
            if(it==ProperNounClass.NETWORK_FAIL){
                Util.easyToast("网络申请失败")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.announcementBack->{
                this.onBackPressed()
            }
        }
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.announcement_activity_menu,menu)
        return true
    }
    fun replaceFragment(topic:String,context:String,time:String){
        val tempFragment = AnnouncementDetailTextFragment()
        val bundle = Bundle()
        bundle.putString("topic", topic)
        bundle.putString("context", context)
        bundle.putString("time", time)
        tempFragment.arguments = bundle
        Util.repLaceFragment(tempFragment,true,this,R.id.announcementFragment)
    }
}