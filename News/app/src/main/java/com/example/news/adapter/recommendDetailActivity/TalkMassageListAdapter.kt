package com.example.news.adapter.recommendDetailActivity



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.news.R
import com.example.news.classes.TalkMassage
import com.example.news.classes.TalkMassage.Companion.CATEGORY_MASSAGE
import com.example.news.classes.TalkMassage.Companion.CATEGORY_MIDDLE_MASSAGE
import com.example.news.classes.TalkMassage.Companion.MIDDLE_MASSAGE
import com.example.news.classes.TalkMassage.Companion.MYSELF_IMAGE
import com.example.news.classes.TalkMassage.Companion.MYSELF_MASSAGE
import com.example.news.classes.TalkMassage.Companion.OTHER_IMAGE
import com.example.news.classes.TalkMassage.Companion.OTHER_MASSAGE
import com.example.news.utilClass.Util
import com.example.news.utilClass.Util.await
import com.example.news.network.RecommendDetailNetwork
import com.example.news.theActivitys.NewsDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TalkMassageListAdapter(private val data: List<TalkMassage>, val activity: NewsDetailActivity, val userId: String?,
                             private val scope: CoroutineScope) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    inner class RightMassageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val talkMassage: TextView = view.findViewById(R.id.talkMassage)
        val userIcon: ImageView=view.findViewById(R.id.userIcon)
    }
    inner class LeftMassageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val talkMassage: TextView = view.findViewById(R.id.talkMassage)
        val userIcon: ImageView=view.findViewById(R.id.userIcon)
    }
    inner class LeftImageViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val talkMassage: ImageView = view.findViewById(R.id.talkMassage)
        val userIcon: ImageView=view.findViewById(R.id.userIcon)
    }
    inner class RightImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val talkMassage: ImageView = view.findViewById(R.id.talkMassage)
        val userIcon: ImageView=view.findViewById(R.id.userIcon)
    }
    inner class Middle(view: View):RecyclerView.ViewHolder(view) {
        val middleText: TextView = view.findViewById(R.id.middleText)
    }
    override fun getItemViewType(position: Int): Int {
        val item=data[position]
        when(item.category){
            CATEGORY_MIDDLE_MASSAGE->{
                return MIDDLE_MASSAGE
            }
            CATEGORY_MASSAGE->{
                return if(userId==item.userId.toString()){
                    MYSELF_MASSAGE
                } else{
                    OTHER_MASSAGE
                }
            }
            else->{
                return if(userId==item.userId.toString()){
                    MYSELF_IMAGE
                }else{
                    OTHER_IMAGE
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        when(viewType){
            MIDDLE_MASSAGE->{
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.talk_middle_item, parent, false)
                Middle(view)
            }
            MYSELF_MASSAGE->{
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.talk_right_item, parent, false)
                RightMassageViewHolder(view)
            }
            OTHER_MASSAGE->{
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.talk_left_item, parent, false)
                LeftMassageViewHolder(view)
            }
            MYSELF_IMAGE->{
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.talk_right_image_item, parent, false)
                RightImageViewHolder(view)
            }
            else->{
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.talk_left_image_item, parent, false)
                LeftImageViewHolder(view)
            }
        }



    override fun getItemCount(): Int =data.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item=data[position]
        when(holder){
            is RightMassageViewHolder->{
                holder.talkMassage.text=item.content
                scope.launch {
                    val icon=RecommendDetailNetwork.recommendDetailService.getUserDataById(item.userId.toString()).await()
                    try {
                        withContext(Dispatchers.Main){
                        Glide.with(activity)
                            .load(icon.data?.user?.image)
                            .into(holder.userIcon)
                        }
                    }
                    catch (e:Exception){
                        Util.logDetail("on the recyclerview talk",e.message.toString())
                    }
                }
            }
            is LeftMassageViewHolder->{
                holder.talkMassage.text=item.content
                scope.launch {
                    val icon=RecommendDetailNetwork.recommendDetailService.getUserDataById(item.userId.toString()).await()
                    try {
                        withContext(Dispatchers.Main){
                            Glide.with(activity)
                                .load(icon.data?.user?.image)
                                .into(holder.userIcon)
                        }
                    }
                    catch (e:Exception){
                        Util.logDetail("on the recyclerview talk",e.message.toString())
                    }
                }
            }
            is Middle->{
                holder.middleText.text="当前在线人数 ${item.number}"
            }
            is LeftImageViewHolder->{
                scope.launch {
                    val icon=RecommendDetailNetwork.recommendDetailService.getUserDataById(item.userId.toString()).await()
                    try {
                        withContext(Dispatchers.Main){
                            Glide.with(activity)
                                .load(icon.data?.user?.image)
                                .into(holder.userIcon)
                            Glide.with(activity)
                                .load(item.content)
                                .override(500, 500)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(holder.talkMassage)

                        }
                    }
                    catch (e:Exception){
                        Util.logDetail("on the recyclerview talk",e.message.toString())
                    }
                }
            }
            is RightImageViewHolder->{
                scope.launch {
                    val icon=RecommendDetailNetwork.recommendDetailService.getUserDataById(item.userId.toString()).await()
                    try {
                        withContext(Dispatchers.Main){
                            Glide.with(activity)
                                .load(icon.data?.user?.image)
                                .into(holder.userIcon)
                            Glide.with(activity)
                                .load(item.content)
                                .override(500, 500)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(holder.talkMassage)
                        }
                    }
                    catch (e:Exception){
                        Util.logDetail("on the recyclerview talk",e.message.toString())
                    }
                }
            }
        }
    }
}