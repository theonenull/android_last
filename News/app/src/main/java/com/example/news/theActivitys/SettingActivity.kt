package com.example.news.theActivitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.news.R
import com.example.news.fragments.settingActivity.SettingsFragment

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settingFragment, SettingsFragment())
            .commit()
    }
}