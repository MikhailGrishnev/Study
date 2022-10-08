package com.belkinapps.wsrfood.adapters

import GridSpacingItemDecoration
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.*
import com.belkinapps.wsrfood.R
import com.belkinapps.wsrfood.data.responses.Item

class MenuPagerAdapter(var items: MutableList<Item>, var pref: SharedPreferences?) : RecyclerView.Adapter<MenuPagerVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuPagerVH =
        MenuPagerVH(LayoutInflater.from(parent.context).inflate(R.layout.menu, parent, false))

    override fun getItemCount(): Int = 4


    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: MenuPagerVH, position: Int) = holder.itemView.run {

        if(position == 0){
            holder.menuRecycler.layoutManager = GridLayoutManager(context, 2)
            holder.menuRecycler.addItemDecoration(GridSpacingItemDecoration(2, 35, true, 0))
            var foods = mutableListOf<Item>()
            for (i in 0..items.size-1) {
                if (items[i].category == "Foods") {
                    foods.add(items[i])
                }
            }
            holder.menuRecycler.adapter = MenuRecyclerAdapter(foods, pref)
        }

        if(position == 1) {
            holder.menuRecycler.layoutManager = GridLayoutManager(context, 2)
            holder.menuRecycler.addItemDecoration(GridSpacingItemDecoration(2, 35, true, 0))
            var drinks = mutableListOf<Item>()
            for (i in 0..items.size-1) {
                if (items[i].category == "Drinks") {
                    drinks.add(items[i])
                }
            }
            holder.menuRecycler.adapter = MenuRecyclerAdapter(drinks, pref)
        }

        if (position == 2) {
            holder.menuRecycler.layoutManager = GridLayoutManager(context, 2)
            holder.menuRecycler.addItemDecoration(GridSpacingItemDecoration(2, 35, true, 0))
            var snacks = mutableListOf<Item>()
            for (i in 0..items.size-1) {
                if (items[i].category == "Snacks") {
                    snacks.add(items[i])
                }
            }
            holder.menuRecycler.adapter = MenuRecyclerAdapter(snacks, pref)
        }

        if (position == 3) {
            holder.menuRecycler.layoutManager = GridLayoutManager(context, 2)
            holder.menuRecycler.addItemDecoration(GridSpacingItemDecoration(2, 35, true, 0))
            var sauces = mutableListOf<Item>()
            for (i in 0..items.size-1) {
                if (items[i].category == "Sauce") {
                    sauces.add(items[i])
                }
            }
            holder.menuRecycler.adapter = MenuRecyclerAdapter(sauces, pref)
        }
    }

}

class MenuPagerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val menuRecycler: RecyclerView = itemView.findViewById(R.id.menuRecycler)
}