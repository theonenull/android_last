package com.example.news.adapter.mainActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.utilClass.Util
import com.example.news.classes.network.recommendNetwork.Records
import com.example.news.theActivitys.NewsDetailActivity


class TrialListAdapter(private val bookList: List<Records>, var context: Context) : RecyclerView.Adapter<TrialListAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewRecommendTitle: TextView = view.findViewById(R.id.recommendItemTitle)
        val textViewRecommendText: TextView = view.findViewById(R.id.recommendItemText)
        val imageViewRecommendItem:ImageView = view.findViewById(R.id.recommendItemImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.trial_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = bookList[position]
        holder.textViewRecommendTitle.text = item.spotName
        holder.textViewRecommendText.text = item.summary
        if(item.image==null){
            holder.imageViewRecommendItem.setImageResource(R.drawable.hot)
        }
        else{
            Glide.with(context)
                .load(item.image)
                .into(holder.imageViewRecommendItem);

        }
//        holder.imageViewRecommendItem.
        holder.textViewRecommendTitle.setOnClickListener {
            enterDetail(item)
        }
        holder.textViewRecommendText.setOnClickListener {
            enterDetail(item)
        }
        holder.imageViewRecommendItem.setOnClickListener {
            enterDetail(item)
        }

    }

    override fun getItemCount(): Int =bookList.size

    private fun enterDetail(item:Records){
        Util.startIntent(context, NewsDetailActivity::class.java,
            mapOf
                (
                "userId" to item.userId.toString(),
                "spotId" to item.spotId.toString(),
            )
        )
    }
}