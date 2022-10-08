package com.belkinapps.wsrfood.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import com.belkinapps.wsrfood.R
import com.belkinapps.wsrfood.activities.MainActivity
import com.belkinapps.wsrfood.activities.OneItemActivity
import com.belkinapps.wsrfood.data.responses.Item
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class MenuRecyclerAdapter(var items: MutableList<Item>, var pref: SharedPreferences?) : RecyclerView.Adapter<MenuRecyclerVH>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(filteredList: MutableList<Item>){
        this.items = filteredList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuRecyclerVH =
        MenuRecyclerVH(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MenuRecyclerVH, position: Int) {
        holder.nameDish.text = items[position].nameDish
        holder.price.text = "N${items[position].price}"
        Picasso.get()
            .load("https://food.madskill.ru/up/images/${items[position].icon}")
            .into(holder.dishIcon)
        holder.itemView.setOnClickListener {
            var oneItem: MutableList<Item> = mutableListOf(items[position])
            SaveData(oneItem)
            val myIntent = Intent(holder.itemView.getContext(), OneItemActivity::class.java)
            holder.itemView.getContext().startActivity(myIntent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun SaveData(oneItem: MutableList<Item>) {
        val editorList = pref
        editorList?.putList("oneItem", oneItem)
        val editor = pref?.edit()
        editor?.apply()
    }

    fun <T> SharedPreferences.putList(spListKey: String, list: MutableList<T>) {
        val listJson = Gson().toJson(list)
        edit {
            putString(spListKey, listJson)
        }
    }

}

class MenuRecyclerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameDish: TextView = itemView.findViewById(R.id.nameDish)
    val price: TextView = itemView.findViewById(R.id.price)
    val dishIcon: ImageView = itemView.findViewById(R.id.dishIcon)

}