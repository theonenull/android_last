package com.example.news.adapter.mainActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.utilClass.Util
import com.example.news.classes.network.categoryDetailNetwork.Records
import com.example.news.keyValue.LOGIXDATA
import com.example.news.theActivitys.MainActivity
import com.example.news.theActivitys.NewsDetailActivity

class CategoryDetailAdapter(private val categoryDetailList: List<Records>, val activity: MainActivity) : RecyclerView.Adapter<CategoryDetailAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewRecommendTitle: TextView = view.findViewById(R.id.recommendItemTitle)
        val textViewRecommendText: TextView = view.findViewById(R.id.recommendItemText)
        val imageViewRecommendItem: ImageView = view.findViewById(R.id.recommendItemImage)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = categoryDetailList[position]
        holder.textViewRecommendTitle.text = item.spotName
        holder.textViewRecommendText.text = item.summary
        if(item.image==null){
            holder.imageViewRecommendItem.setImageResource(R.drawable.hot)
        }
        else{
            Glide.with(activity)
                .load(item.image)
                .into(holder.imageViewRecommendItem);
        }

        holder.textViewRecommendTitle.setOnClickListener {
            if(Util.getSharePreferencesString(null, LOGIXDATA.PLACE, LOGIXDATA.ISLOGIN) == "true"){
                enterDetail(item)
            }
            else{
                Util.easyToast("?????????")
            }
        }
        holder.textViewRecommendText.setOnClickListener {
            if(Util.getSharePreferencesString(null, LOGIXDATA.PLACE, LOGIXDATA.ISLOGIN) == "true"){
                enterDetail(item)
            }
            else{
                Util.easyToast("?????????")
            }
        }
        holder.imageViewRecommendItem.setOnClickListener {
            if(Util.getSharePreferencesString(null, LOGIXDATA.PLACE, LOGIXDATA.ISLOGIN) == "true"){
                enterDetail(item)
            }
            else{
                Util.easyToast("?????????")
            }
        }
    }

    override fun getItemCount(): Int =categoryDetailList.size
    private fun enterDetail(item:Records){
        Util.startIntent(activity, NewsDetailActivity::class.java,
            mapOf
                (
                "userId" to item.userId.toString(),
                "spotId" to item.spotId.toString(),
            )
        )
    }
}