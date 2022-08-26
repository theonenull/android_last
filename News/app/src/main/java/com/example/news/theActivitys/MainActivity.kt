package com.example.news.theActivitys

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.utilClass.Util
import com.example.news.classes.network.loginNetwork.User
import com.example.news.databinding.ActivityMainBinding
import com.example.news.fragments.mainActivity.CategoryDetailFragment
import com.example.news.fragments.mainActivity.CategoryInsertFragment
import com.example.news.fragments.mainActivity.RecommendFragment
import com.example.news.keyValue.LOGIXDATA
import com.example.news.keyValue.USERDATA
import com.example.news.viewModel.RecommendViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!
    private lateinit var viewModel:RecommendViewModel
    var userData: User? =null
    private val startActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //此处是跳转的result回调方法
            if (it.data != null && it.resultCode == Activity.RESULT_OK) {
                val data = Json.decodeFromString<User>(it.data!!.getStringExtra("userData").toString())
                Glide.with(this).load(data.image).into(headImage)
                headTextView.text=data.nickname
        }
    }

    lateinit var view:View
    lateinit var headImage:ImageView
    lateinit var headTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        view= binding.navView.inflateHeaderView(R.layout.nav_header)
        headImage= view.findViewById(R.id.iconImage)
        headTextView=view.findViewById(R.id.mailText)
        viewModel= ViewModelProvider(this).get(RecommendViewModel::class.java)
//        Util.repLaceFragment(RecommendFragment(),false,this,R.id.RecommendFragment)
        //初始推荐
        repLaceFragment(RecommendFragment(viewModel),false)
//        //隐藏通知栏
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //支持标题栏
        val toolbar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        //侧边栏键
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.drawer_start)
        }
        //侧边栏选项
        navAction()
        displayTheUserData()
        //设置沉浸式
        windowSetting()
    }
    //侧边栏选项
    private fun navAction(){
        val nav=findViewById<NavigationView>(R.id.navView)
        nav.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.sign_in ->{
                    if(Util.getSharePreferencesString(this,LOGIXDATA.PLACE,LOGIXDATA.ISLOGIN) != "true"){
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        binding.drawerLayout.closeDrawers()
                        startActivity.launch(intent)
                    }
                    else{
                        Util.easyToast("已登录，若需切换账号，请退出登录")
                    }
                }
                R.id.sign_out ->{
                    binding.drawerLayout.closeDrawers()
                    Util.showInformation("确定退出登录吗?这将删除您的所有本地数据",this) {
                        Util.clearSharePreference(this,USERDATA.PLACE)
                        val map= mapOf(
                            LOGIXDATA.PASSWORD to "",
                            LOGIXDATA.PASSWORD to "",
                            LOGIXDATA.ISLOGIN to "false"
                        )
                        Util.setSharePreferences(this,LOGIXDATA.PLACE,null,map)
                        displayTheUserData()
                    }
                }
                R.id.setting ->{
                    Toast.makeText(this,"设置", Toast.LENGTH_SHORT).show()
                    val intent= Intent(this,SettingActivity::class.java)
                    startActivity(intent)
                    binding.drawerLayout.closeDrawers()
                }
                R.id.test ->{
                    Util.logDetail("测试专用","测试开启")
                    /*打开图库*/
                    /*打开图库*/
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "image/*"
                    //处理返回集
                    //处理返回集
                    startActivityForResult(intent, 2)
                    binding.drawerLayout.closeDrawers()
                }
                R.id.person->{
                    if(Util.getSharePreferencesString(this,LOGIXDATA.PLACE,LOGIXDATA.ISLOGIN) == "true"){
                        binding.drawerLayout.closeDrawers()
                        Util.startIntent(this,PersonActivity::class.java,null)
                    }
                    else{
                        Util.easyToast("未登录")
                    }
                }
                R.id.announcement->{
                    Util.startIntent(this,AnnouncementActivity::class.java,null)
//                    val intent= Intent(this,PersonActivity::class.java)
//                    startActivity(intent)
                    binding.drawerLayout.closeDrawers()
                }
            }
            true
        }
    }
    //切换fragment
    private fun repLaceFragment(fragment: Fragment, boolean: Boolean){
        val fragmentManager = supportFragmentManager
        val transaction=fragmentManager.beginTransaction()
        transaction.replace(R.id.RecommendFragment,fragment)
        if(boolean){
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
    //暂停隐藏通知栏
//    override fun onResume() {
//        super.onResume()
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN)
//    }
    //填充标题栏菜单
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }
    //标题栏选项
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->binding.drawerLayout.openDrawer(GravityCompat.START)
            R.id.category-> {
                repLaceFragment(com.example.news.fragments.mainActivity.CategoryFragment(this.viewModel),true)
            }
            R.id.search-> {
                repLaceFragment(com.example.news.fragments.mainActivity.RecommendFoundFragment(this.viewModel),true)
            }
            R.id.add ->{
                endFirstPage()
            }
        }
        return true
    }
    private fun endFirstPage(){
        try {
            this.supportFragmentManager.popBackStackImmediate(null,1)
        }catch (e:Exception){
            repLaceFragment(RecommendFragment(viewModel),false)
        }
    }

    fun getIntoCateGoryDetail(spotSort:String,description:String) {
        repLaceFragment(CategoryDetailFragment(this.viewModel, spotSort, description),true)
    }

    private fun displayTheUserData(){
        if(Util.getSharePreferencesString(this,LOGIXDATA.PLACE,LOGIXDATA.ISLOGIN)=="true"){
            this.userData=User(
                Util.getSharePreferencesString(this,USERDATA.PLACE,USERDATA.USER_ID).toInt(),
                Util.getSharePreferencesString(this,USERDATA.PLACE,USERDATA.ROLE),
                null,
                null,
                Util.getSharePreferencesString(this,USERDATA.PLACE,USERDATA.NICKNAME),
                Util.getSharePreferencesString(this,USERDATA.PLACE,USERDATA.SALT),
                Util.getSharePreferencesString(this,USERDATA.PLACE,USERDATA.IMAGE),
                Util.getSharePreferencesString(this,USERDATA.PLACE,USERDATA.AGE)
            )
            Glide.with(this).load(userData!!.image).into(headImage)
            headTextView.text= userData!!.nickname
        }
        else{
            headImage.setImageResource(R.drawable.header_first)
            headTextView.text="未登录"
        }
    }
    private fun windowSetting(){
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.statusBarColor = resources.getColor(R.color.purple_200);
    }

    //判断是否联网
    private fun checkConnectNetwork(context: Context): Boolean {
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val net = conn.activeNetworkInfo
        return net != null && net.isConnected
    }

    fun replaceFragmentToAddCateGory(userId:String,viewModel:RecommendViewModel){
        Util.repLaceFragment(CategoryInsertFragment(userId,viewModel),true,this,R.id.RecommendFragment)
    }
    override fun onResume() {
        super.onResume()
        displayTheUserData()
    }
}