package com.belkinapps.wsrfood.adapters

import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import com.belkinapps.wsrfood.R
import com.belkinapps.wsrfood.activities.CurrentHistoryOrderActivity
import com.belkinapps.wsrfood.activities.SignInActivity
import com.belkinapps.wsrfood.data.requests.DishesOrder
import com.belkinapps.wsrfood.data.requests.OrderRequest
import com.belkinapps.wsrfood.data.responses.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso

class HistoryItemsRecycler(var history: List<OrderRequest>, var dishesList: MutableList<Item>) : RecyclerView.Adapter<HistoryItemsRecyclerVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemsRecyclerVH =
        HistoryItemsRecyclerVH(LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false))

    override fun getItemCount(): Int {
        return history.size
    }

    override fun onBindViewHolder(holder: HistoryItemsRecyclerVH, position: Int) {
        holder.adress.text = history[position].address
        holder.date.text = history[position].date
        var orderPrice = 0
        for (item: DishesOrder in history[position].dishes) {
            for (dish: Item in dishesList) {
                if (item.dishId == dish.dishId){
                    orderPrice += item.count * dish.price.toInt()
                }
            }
        }
        holder.cost.text = orderPrice.toString()
        holder.itemView.setOnClickListener {
            val myIntent = Intent(holder.itemView.getContext(), CurrentHistoryOrderActivity::class.java)
            myIntent.putExtra("historyPosition", position)
            holder.itemView.getContext().startActivity(myIntent)
        }
    }
}

class HistoryItemsRecyclerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val adress: TextView = itemView.findViewById(R.id.history_item_adress)
    val date: TextView = itemView.findViewById(R.id.history_item_date)
    val cost: TextView = itemView.findViewById(R.id.history_item_cost)

}