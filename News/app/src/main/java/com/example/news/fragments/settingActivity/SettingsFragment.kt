package com.example.news.fragments.settingActivity


import android.os.Bundle
import android.text.TextUtils
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import com.example.news.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_demo, rootKey)

        val countingPreference: EditTextPreference? = findPreference("counting")
        countingPreference?.summaryProvider =
            Preference.SummaryProvider<EditTextPreference> { preference ->
                val text = preference.text
                if (TextUtils.isEmpty(text)) {
                    "Not set"
                } else {
                    "Length of saved value: " + text.length
                }
            }
    }
}