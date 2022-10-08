package com.belkinapps.wsrfood.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.belkinapps.wsrfood.R
import com.belkinapps.wsrfood.data.requests.DishesOrder
import com.belkinapps.wsrfood.data.requests.OrderRequest
import com.belkinapps.wsrfood.data.responses.Item
import com.squareup.picasso.Picasso

class CurrentHistoryRecyclerAdapter(var history: OrderRequest, var dishesList: MutableList<Item>) : RecyclerView.Adapter<CurrentHistoryRecyclerVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentHistoryRecyclerVH =
        CurrentHistoryRecyclerVH(LayoutInflater.from(parent.context).inflate(R.layout.current_history_item, parent, false))

    override fun getItemCount(): Int {
        return history.dishes.size
    }

    override fun onBindViewHolder(holder: CurrentHistoryRecyclerVH, position: Int) {
        var orderPrice = 0
        for (dish: Item in dishesList) {
            if (history.dishes[position].dishId == dish.dishId){
                holder.name.text = dish.nameDish
                holder.counter.text = history.dishes[position].count.toString()
                orderPrice += history.dishes[position].count * dish.price.toInt()
                Picasso.get()
                    .load("https://food.madskill.ru/up/images/${dish.icon}")
                    .into(holder.icon)
            }
        }

        holder.cost.text = orderPrice.toString()

    }
}

class CurrentHistoryRecyclerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.history_dishName)
    val counter: TextView = itemView.findViewById(R.id.history_dish_itemCounter)
    val cost: TextView = itemView.findViewById(R.id.history_dishPrice)
    val icon: ImageView = itemView.findViewById(R.id.history_dish_icon)

}