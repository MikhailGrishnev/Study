package com.belkinapps.wsrfood.adapters

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import com.belkinapps.wsrfood.R
import com.belkinapps.wsrfood.activities.MainActivity
import com.belkinapps.wsrfood.activities.OrderActivity
import com.belkinapps.wsrfood.data.requests.DishesOrder
import com.belkinapps.wsrfood.data.responses.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso

class ExtraItemRecyclerAdapter(val dishesList: MutableList<Item>, val pref: SharedPreferences?, val updOrderList: () -> Unit, val orderList: MutableList<DishesOrder>, val updItemCounter: () -> Unit) : RecyclerView.Adapter<ExtraItemRecyclerVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtraItemRecyclerVH =
        ExtraItemRecyclerVH(LayoutInflater.from(parent.context).inflate(R.layout.extra_item, parent, false))

    override fun onBindViewHolder(holder: ExtraItemRecyclerVH, position: Int) {
        holder.name.text = dishesList[position].nameDish
        holder.price.text = dishesList[position].price
        holder.icon.setOnClickListener {

            var inOrder = false
            notifyDataSetChanged()
            for (item: DishesOrder in orderList) {
                if (item.dishId == dishesList[position].dishId) {
                    item.count += 1
                    SaveData(orderList)
                    updItemCounter.invoke()
                    inOrder = true
                }
            }

            if (!inOrder) {
                pref?.addItemToList("DishesOrder", DishesOrder(dishesList[position].dishId, 1))
                updOrderList.invoke()
            }

        }
        Picasso.get()
            .load("https://food.madskill.ru/up/images/${dishesList[position].icon}")
            .into(holder.icon)
    }

    override fun getItemCount(): Int {
        return dishesList.size
    }

    inline fun <reified T> SharedPreferences.addItemToList(spListKey: String, item: T) {
        val savedList = getList<T>(spListKey).toMutableList()
        savedList.add(item)
        val listJson = Gson().toJson(savedList)
        edit { putString(spListKey, listJson) }
    }

    fun SaveData(orderList: MutableList<DishesOrder>) {
        val editorList = pref
        editorList?.putList("DishesOrder", orderList)
        val editor = pref?.edit()
        editor?.apply()
    }

    fun <T> SharedPreferences.putList(spListKey: String, list: MutableList<T>) {
        val listJson = Gson().toJson(list)
        edit {
            putString(spListKey, listJson)
        }
    }

    inline fun <reified T> SharedPreferences.getList(spListKey: String): MutableList<T> {
        val listJson = getString(spListKey, "")
        if (!listJson.isNullOrBlank()) {
            val type = object : TypeToken<MutableList<T>>() {}.type
            return Gson().fromJson(listJson, type)
        }
        return mutableListOf()
    }
}

class ExtraItemRecyclerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val icon: ImageView = itemView.findViewById(R.id.extraItem_icon)
    val name: TextView = itemView.findViewById(R.id.extraItem_name)
    val price: TextView = itemView.findViewById(R.id.extraItem_price)

}