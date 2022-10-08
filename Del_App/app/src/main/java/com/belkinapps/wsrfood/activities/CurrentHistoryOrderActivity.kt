package com.belkinapps.wsrfood.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.belkinapps.wsrfood.App
import com.belkinapps.wsrfood.R
import com.belkinapps.wsrfood.adapters.CurrentHistoryRecyclerAdapter
import com.belkinapps.wsrfood.adapters.HistoryItemsRecycler
import com.belkinapps.wsrfood.data.remote.FoodApi
import com.belkinapps.wsrfood.data.requests.DishesOrder
import com.belkinapps.wsrfood.data.requests.OrderRequest
import com.belkinapps.wsrfood.data.responses.Item
import com.belkinapps.wsrfood.databinding.ActivityCurrentHistoryOrderBinding
import com.belkinapps.wsrfood.databinding.ActivityHistoryBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class CurrentHistoryOrderActivity () : AppCompatActivity() {

    private lateinit var binding: ActivityCurrentHistoryOrderBinding
    var pref: SharedPreferences? = null
    private val compositeDisposable = CompositeDisposable()
    lateinit var foodApi: FoodApi

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        binding = ActivityCurrentHistoryOrderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val dishesList: MutableList<Item> = pref?.getMutableList<Item>("menu") as MutableList<Item>
        val historyList: List<OrderRequest> = pref?.getList<OrderRequest>("HistoryList") as List<OrderRequest>
        val historyPosition = intent.getIntExtra("historyPosition", 0)
        val history = historyList[historyPosition]
        val recyclerView = binding.currentHistoryItems
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = CurrentHistoryRecyclerAdapter(history, dishesList)

        val adress = binding.currentHistoryAdress
        val date = binding.currentHistoryDate
        val totalPrice = binding.chOrderPrice
        adress.text = history.address
        date.text = history.date

        var orderPrice = 0
        for (item: DishesOrder in history.dishes) {
            for (dish: Item in dishesList) {
                if (item.dishId == dish.dishId){
                    orderPrice += item.count * dish.price.toInt()
                }
            }
        }
        totalPrice.text = orderPrice.toString()

        val repeatBtn = binding.repeatOrderBtn
        repeatBtn.setOnClickListener {
            foodApi = (application as? App)?.foodApi!!
            compositeDisposable.add(foodApi.sendOrderRequest(
                OrderRequest(
                    history.address,
                    getCurrentDate(),
                    history.dishes
                )
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }, {

                }))
        }

        val homeBtn = binding.currentHistoryHome
        homeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val orderBtn = binding.currentHistoryCart
        orderBtn.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }
    }

    inline fun <reified T> SharedPreferences.getMutableList(spListKey: String): MutableList<T> {
        val listJson = getString(spListKey, "")
        if (!listJson.isNullOrBlank()) {
            val type = object : TypeToken<MutableList<T>>() {}.type
            return Gson().fromJson(listJson, type)
        }
        return mutableListOf()
    }

    inline fun <reified T> SharedPreferences.getList(spListKey: String): List<T> {
        val listJson = getString(spListKey, "")
        if (!listJson.isNullOrBlank()) {
            val type = object : TypeToken<List<T>>() {}.type
            return Gson().fromJson(listJson, type)
        }
        return listOf()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    fun getCurrentDate():String{
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")
        return sdf.format(Date())
    }
}