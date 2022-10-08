package com.belkinapps.wsrfood.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import com.belkinapps.wsrfood.R
import com.belkinapps.wsrfood.data.requests.DishesOrder
import com.belkinapps.wsrfood.data.responses.Item
import com.belkinapps.wsrfood.databinding.ActivityMainBinding
import com.belkinapps.wsrfood.databinding.ActivityOneItemBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso

class OneItemActivity : AppCompatActivity() {

    var counter = 1
    var pref: SharedPreferences? = null
    private lateinit var binding: ActivityOneItemBinding
    var oneItem: MutableList<Item> = mutableListOf()
    var dishesOrder: MutableList<DishesOrder> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOneItemBinding.inflate(layoutInflater)
        val view = binding.root
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        setContentView(view)

        oneItem = pref?.getList<Item>("oneItem") as MutableList<Item>
        dishesOrder = pref?.getList<DishesOrder>("DishesOrder") as MutableList<DishesOrder>

        val dishIcon = binding.oneItemDishIcon
        val backBtn = binding.oneItemBackBtn
        val addBtn = binding.oneItemAddBtn
        val nameDish = binding.oneItemName
        val price = binding.oneItemPrice
        val itemCounter = binding.oneItemItemCounter
        val plusBtn = binding.oneItemPlusBtn
        val minusBtn = binding.oneItemMinusBtn

        Picasso.get()
            .load("https://food.madskill.ru/up/images/${oneItem[0].icon}")
            .into(dishIcon)
        nameDish.text = oneItem[0].nameDish
        price.text = oneItem[0].price

        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        plusBtn.setOnClickListener {
            counter += 1
            itemCounter.text = counter.toString()
        }

        minusBtn.setOnClickListener {
            counter -= 1
            itemCounter.text = counter.toString()
        }

        var isSelected = false

        addBtn.setOnClickListener {
            for (item: DishesOrder in dishesOrder){
                if (item.dishId == oneItem[0].dishId){
                    isSelected = true
                    var prevCounter = dishesOrder[dishesOrder.indexOf(item)].count
                    dishesOrder.removeAt(dishesOrder.indexOf(item))
                    dishesOrder.add(DishesOrder(oneItem[0].dishId, prevCounter+counter))
                }
            }

            if (!isSelected) {
                dishesOrder.add(DishesOrder(oneItem[0].dishId, counter))
            }

            SaveData(dishesOrder)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun SaveData(dishesOrder: MutableList<DishesOrder>) {
        val editorList = pref
        editorList?.putList("DishesOrder", dishesOrder)
        val editor = pref?.edit()
        editor?.apply()
    }

    inline fun <reified T> SharedPreferences.getList(spListKey: String): MutableList<T> {
        val listJson = getString(spListKey, "")
        if (!listJson.isNullOrBlank()) {
            val type = object : TypeToken<MutableList<T>>() {}.type
            return Gson().fromJson(listJson, type)
        }
        return mutableListOf()
    }

    fun <T> SharedPreferences.putList(spListKey: String, list: MutableList<T>) {
        val listJson = Gson().toJson(list)
        edit {
            putString(spListKey, listJson)
        }
    }
}