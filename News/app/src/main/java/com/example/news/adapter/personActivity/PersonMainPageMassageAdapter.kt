package com.example.news.adapter.personActivity



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.utilClass.Util
import com.example.news.theActivitys.PersonActivity

class PersonMainPageMassageAdapter (private val data: Util.PersonMassage, val activity: PersonActivity) : RecyclerView.Adapter<PersonMainPageMassageAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val buttonText: TextView = view.findViewById(R.id.text)
        val buttonImage: ImageView = view.findViewById(R.id.icon)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.person_main_page_massage_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemOfIcon = data.listOfIcon[position]
        val itemOfText = data.listOfMassage[position]
        holder.buttonText.text=itemOfText
        holder.buttonImage.setImageResource(itemOfIcon)
    }
    override fun getItemCount(): Int =data.count
    class ButtonInPersonMainPage(var image:Int,var text:String){}
}