package com.belkinapps.wsrfood.adapters

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.belkinapps.wsrfood.R
import com.belkinapps.wsrfood.data.requests.DishesOrder
import com.belkinapps.wsrfood.data.responses.Item
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso

class OrderRecyclerAdapter (var dishesList: MutableList<Item>, var pref: SharedPreferences?, var totalPrice: TextView) : RecyclerView.Adapter<OrderRecyclerVH>()  {

    var orderList: MutableList<DishesOrder> = pref?.getList<DishesOrder>("DishesOrder") as MutableList<DishesOrder>
    val updOrderList = {orderList = pref?.getList<DishesOrder>("DishesOrder") as MutableList<DishesOrder>; notifyItemInserted(orderList.size-1); updTotalPrice()}
    val updItemCounter = {orderList = pref?.getList<DishesOrder>("DishesOrder") as MutableList<DishesOrder>; notifyDataSetChanged(); updTotalPrice()}
//    @SuppressLint("NotifyDataSetChanged")
//    fun updOrderList() {
//        orderList = pref?.getList<DishesOrder>("DishesOrder") as MutableList<DishesOrder>
//        notifyDataSetChanged()
//    }

    fun updTotalPrice() {
        var orderPrice = 0
        for (item: DishesOrder in orderList) {
            for (dish: Item in dishesList) {
                if (item.dishId == dish.dishId){
                    orderPrice += item.count * dish.price.toInt()
                }
            }
        }
        totalPrice.text = orderPrice.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderRecyclerVH =
        OrderRecyclerVH(LayoutInflater.from(parent.context).inflate(R.layout.order_items, parent, false))

    override fun onBindViewHolder(holder: OrderRecyclerVH, position: Int) {
        println("позиция1 ${position} orderList.size ${orderList.size}")
        var dishId: String
        if (position != orderList.size+1 && position!= orderList.size) {
            println("позиция ${position}")
            println("1")
            for (item: Item in dishesList) {
                println("2")
                if (orderList[position].dishId == item.dishId) {
                    println("3")
                    var counter = orderList[position].count
                    var dishIndex = dishesList.indexOf(item)
                    holder.dishName.text = dishesList[dishIndex].nameDish
                    holder.dishPrice.text = dishesList[dishIndex].price
                    holder.itemCounter.text = counter.toString()
                    Picasso.get()
                        .load("https://food.madskill.ru/up/images/${dishesList[dishIndex].icon}")
                        .into(holder.icon)
                    holder.minus_btn.setOnClickListener {
                        counter -= 1
                        holder.itemCounter.text = counter.toString()
                        dishId = orderList[position].dishId
                        orderList.removeAt(position)
                        orderList.add(position, DishesOrder(dishId, counter))
                        SaveData(orderList)
                        updTotalPrice()
                    }
                    holder.plus_btn.setOnClickListener {
                        counter += 1
                        holder.itemCounter.text = counter.toString()
                        dishId = orderList[position].dishId
                        println(orderList)
                        orderList.removeAt(position)
                        orderList.add(position, DishesOrder(dishId, counter))
                        SaveData(orderList)
                        println(orderList)
                        updTotalPrice()
                    }
                }
            }
        } else {
            if (position != orderList.size) {
                holder.icon.visibility = View.GONE
                holder.dishName.visibility = View.GONE
                holder.dishPrice.visibility = View.GONE
                holder.minus_btn.visibility = View.GONE
                holder.plus_btn.visibility = View.GONE
                holder.itemCounter.visibility = View.GONE
                holder.iconCard.visibility = View.INVISIBLE
                holder.extraText.visibility = View.VISIBLE
                holder.extraRecycler.visibility = View.VISIBLE
                holder.extraRecycler.layoutManager = LinearLayoutManager(holder.itemView.getContext(), RecyclerView.HORIZONTAL, false)
                holder.extraRecycler.adapter = ExtraItemRecyclerAdapter(dishesList, pref, updOrderList, orderList, updItemCounter)
            }

            if (position != orderList.size+1) {
                holder.icon.visibility = View.GONE
                holder.dishName.visibility = View.GONE
                holder.dishPrice.visibility = View.GONE
                holder.minus_btn.visibility = View.GONE
                holder.plus_btn.visibility = View.GONE
                holder.itemCounter.visibility = View.GONE
                holder.iconCard.visibility = View.INVISIBLE
                holder.extraText.visibility = View.VISIBLE
                holder.extraRecycler.visibility = View.VISIBLE
                var sauces = mutableListOf<Item>()
                for (i in 0..dishesList.size-1) {
                    if (dishesList[i].category == "Sauce") {
                        sauces.add(dishesList[i])
                    }
                }
                holder.extraRecycler.layoutManager = LinearLayoutManager(holder.itemView.getContext(), RecyclerView.HORIZONTAL, false)
                holder.extraRecycler.adapter = ExtraItemRecyclerAdapter(sauces, pref, updOrderList, orderList, updItemCounter)
                holder.extraText.text = "Соусы"
            }
        }
        updTotalPrice()
    }

    override fun getItemCount(): Int {
        return orderList.size+2
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

class OrderRecyclerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val icon: ImageView = itemView.findViewById(R.id.order_icon)
    val dishName: TextView = itemView.findViewById(R.id.order_dishName)
    val dishPrice: TextView = itemView.findViewById(R.id.order_dishPrice)
    val minus_btn: ImageButton = itemView.findViewById(R.id.order_minus_btn)
    val plus_btn: ImageButton = itemView.findViewById(R.id.order_plus_btn)
    val itemCounter: TextView = itemView.findViewById(R.id.order_itemCounter)
    val extraText: TextView = itemView.findViewById(R.id.extraText)
    val extraRecycler: RecyclerView = itemView.findViewById(R.id.extraRecycler)
    val iconCard: MaterialCardView = itemView.findViewById(R.id.materialCardView2)
}