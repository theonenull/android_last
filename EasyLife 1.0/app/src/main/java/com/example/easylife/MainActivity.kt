package com.example.easylife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.example.easylife.databinding.ActivityMainBinding
lateinit var binding : ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val root=binding.root
        setContentView(root)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.drawer)
        }
        val nav=binding.navView
        navAction()
    }

    //为toolbar添加menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    //为菜单添加逻辑
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->binding.drawerLayout.openDrawer(GravityCompat.START)
            R.id.exit-> Toast.makeText(this,"退出",Toast.LENGTH_SHORT).show()
            R.id.about-> Toast.makeText(this,"关于作者",Toast.LENGTH_SHORT).show()

        }
        return true
    }

    fun navAction(){
        val nav=binding.navView
        nav.setNavigationItemSelectedListener{it->
            when(it.itemId){
                R.id.nav_picture_out_school->{
                    Toast.makeText(this,"出行码",Toast.LENGTH_SHORT).show()
                    val intent=Intent(this,SchoolOutPictureActivity::class.java)
                    startActivity(intent)
                }
                R.id.show_weather->{
                    Toast.makeText(this,"天气预览",Toast.LENGTH_SHORT).show()

                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }
}