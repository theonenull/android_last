package com.example.news.adapter.mainActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.classes.network.categoryNetwork.Records
import com.example.news.theActivitys.MainActivity


class CategoryAdapter(private val categoryList: List<Records>,val activity: MainActivity) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryName: TextView = view.findViewById(R.id.categoryName)
        val categoryText:TextView=view.findViewById(R.id.categoryText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = categoryList[position]
        holder.categoryName.text = item.spotSort
        holder.categoryText.text = item.spotDescribe
        holder.categoryName.setOnClickListener {
            activity.getIntoCateGoryDetail(item.spotSort.toString(),item.spotDescribe.toString())
        }
    }

    override fun getItemCount(): Int =categoryList.size
}