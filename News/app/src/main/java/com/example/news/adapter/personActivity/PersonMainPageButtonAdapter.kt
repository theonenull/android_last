package com.example.news.adapter.personActivity



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.theActivitys.PersonActivity

class PersonMainPageButtonAdapter (private val buttonList: List<ButtonInPersonMainPage>, val activity: PersonActivity) : RecyclerView.Adapter<PersonMainPageButtonAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val buttonText: TextView = view.findViewById(R.id.buttonText)
        val buttonImage: ImageView = view.findViewById(R.id.buttonImage)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.person_main_page_button_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = buttonList[position]
        holder.buttonText.text = item.text
        holder.buttonImage.setImageResource(item.image)
        if(position==0){
            holder.buttonImage.setOnClickListener {
                activity.replaceFragmentToPersonChangeDataFragment()
            }
        }
    }
    override fun getItemCount(): Int =buttonList.size
    class ButtonInPersonMainPage(var image:Int,var text:String){}
}