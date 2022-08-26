package com.example.news.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.classes.Records
import com.example.news.utilClass.Util
import com.example.news.fragments.announcementActivity.AnnouncementListFragment
import com.example.news.theActivitys.AnnouncementActivity


class AnnouncementListAdapter(private val announcementList: List<Records>, var context: Context, var fragment: AnnouncementListFragment, var activity: AnnouncementActivity) : RecyclerView.Adapter<AnnouncementListAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val announcementTitle: TextView = view.findViewById(R.id.announcementTitle)
        val announcementTime: TextView = view.findViewById(R.id.announcementTime)
        val announcementCard:CardView=view.findViewById(R.id.announcementCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.announcement_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = announcementList[position]
        holder.announcementTitle.text = item.topic
        holder.announcementTime.text = item.time
        holder.announcementCard.setOnClickListener {
            activity.replaceFragment(item.topic,item.context,item.time)
            Util.logDetail("sssssssssssss",item.context)
        }
    }

    override fun getItemCount(): Int =announcementList.size
}