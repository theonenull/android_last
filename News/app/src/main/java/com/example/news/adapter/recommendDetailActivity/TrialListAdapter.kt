package com.example.news.adapter.recommendDetailActivity



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.theActivitys.NewsDetailActivity
import com.example.news.utilClass.UserDataUtil
import com.example.news.utilClass.Util

class TrialListAdapter (private val data: List<com.example.news.classes.network.trialListNetwork.Records>, val activity: NewsDetailActivity) : RecyclerView.Adapter<TrialListAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val buttonText: TextView = view.findViewById(R.id.trialTitle)
//        val buttonImage: ImageView = view.findViewById(R.id.icon)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.trial_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemOfIcon = data[position]
        holder.buttonText.text=itemOfIcon.judgeName
        holder.buttonText.setOnClickListener {
            activity.replaceFragmentToTrialDetail(UserDataUtil.getUserId(),itemOfIcon.judgeId.toString())
        }
//        holder.buttonImage.setImageResource(itemOfIcon)
    }
    override fun getItemCount(): Int =data.size
    class ButtonInPersonMainPage(var image:Int,var text:String){}
}