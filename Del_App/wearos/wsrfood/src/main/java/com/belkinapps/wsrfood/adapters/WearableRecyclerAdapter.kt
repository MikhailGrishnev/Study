package com.belkinapps.wsrfood.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.belkinapps.wsrfood.R
import com.belkinapps.wsrfood.data.requests.DishesOrder
import com.belkinapps.wsrfood.data.responses.Item
import com.squareup.picasso.Picasso

class WearableRecyclerAdapter(var dishesList: MutableList<Item>, var orderList: MutableList<DishesOrder>) : RecyclerView.Adapter<WearableRecyclerVH>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WearableRecyclerVH =
        WearableRecyclerVH(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))

    override fun onBindViewHolder(holder: WearableRecyclerVH, position: Int) {
        for (dish: Item in dishesList) {
            if (orderList[position].dishId == dish.dishId){
                holder.name.text = dish.nameDish
                holder.count.text = orderList[position].count.toString()
                Picasso.get()
                    .load("https://food.madskill.ru/up/images/${dish.icon}")
                    .into(holder.itemImg)
            }
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

}

class WearableRecyclerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.item_name)
    val count: TextView = itemView.findViewById(R.id.item_count)
    val itemImg: ImageView = itemView.findViewById(R.id.item_img)

}