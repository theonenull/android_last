package com.example.news.theActivitys

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.news.R
import com.example.news.utilClass.Util
import com.example.news.classes.network.loginNetwork.User
import com.example.news.utilClass.ImmersiveModeUtils
import com.example.news.fragments.loginActivity.LoginFragment
import com.example.news.fragments.loginActivity.RegisterFragment
import com.example.news.keyValue.LOGIXDATA
import com.example.news.viewModel.LoginViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class LoginActivity : AppCompatActivity(), LoginFragment.MyListener  {
    lateinit var viewModel: LoginViewModel
    override fun sendMassage(){
//        viewModel.dataActivitySet(personName,academyName)personName:String, academyName:String,textRow:String
        this.repLaceFragment(RegisterFragment(this.viewModel),true)
    }
    override fun onSaveInstanceState( outState:Bundle) {
        //保存当前fragment的position
        super.onSaveInstanceState(outState);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        val toolbar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        viewModel=ViewModelProvider(this).get(LoginViewModel::class.java)
        val userName=try{
            if(Util.getSharePreferencesString(this,LOGIXDATA.PLACE,LOGIXDATA.ISLOGIN)!="true"){
                throw(RuntimeException())
            }
            Util.getSharePreferencesString(this,LOGIXDATA.PLACE,LOGIXDATA.USERNAME)
        }catch (e:Exception){
            null
        }
        val passWord=try{
            if(Util.getSharePreferencesString(this,LOGIXDATA.PLACE,LOGIXDATA.ISLOGIN)!="true"){
                throw(RuntimeException())
            }
            Util.getSharePreferencesString(this,LOGIXDATA.PLACE,LOGIXDATA.PASSWORD)
        }catch (e:Exception){
            null
        }
        repLaceFragment(LoginFragment(userName,passWord,viewModel),false)
        //设置沉浸式
        ImmersiveModeUtils.setImmersiveMode(window.decorView);
    }
    fun setResultForUserData(data:User){
        val intent=Intent()
        intent.putExtra("userData", Json.encodeToString(data))
        this.setResult(Activity.RESULT_OK,intent)
        finish()
    }
    private fun repLaceFragment(fragment: Fragment,boolean: Boolean){
        val fragmentManager = supportFragmentManager
        val transaction=fragmentManager.beginTransaction()
        transaction.replace(R.id.loginFragment,fragment)
        if(boolean){
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.login_menu,menu)
        return true
    }
}