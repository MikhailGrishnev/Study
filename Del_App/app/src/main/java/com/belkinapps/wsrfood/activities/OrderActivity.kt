package com.belkinapps.wsrfood.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.belkinapps.wsrfood.App
import com.belkinapps.wsrfood.R
import com.belkinapps.wsrfood.adapters.OrderRecyclerAdapter
import com.belkinapps.wsrfood.data.remote.FoodApi
import com.belkinapps.wsrfood.data.requests.DishesOrder
import com.belkinapps.wsrfood.data.requests.OrderRequest
import com.belkinapps.wsrfood.data.responses.Item
import com.belkinapps.wsrfood.databinding.ActivityOrderBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class OrderActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    lateinit var foodApi: FoodApi
    private lateinit var binding: ActivityOrderBinding
    var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val adress = pref?.getString("adress", "")
        val adressField = binding.orderAdress
        adressField.text = adress

        var orderList: MutableList<DishesOrder> = pref?.getList<DishesOrder>("DishesOrder") as MutableList<DishesOrder>
        var dishesList: MutableList<Item> = pref?.getList<Item>("menu") as MutableList<Item>
        if (!orderList.isEmpty()) {
            val noOrdersCart = binding.noOrdersCart
            val noOrdersText = binding.noOrdersText
            val orderRecycler = binding.orderRecycler
            val totalPrice = binding.materialCardView3
            val makeOrder = binding.makeOrderBtn
            noOrdersCart.visibility = View.GONE
            noOrdersText.visibility = View.GONE
            orderRecycler.visibility = View.VISIBLE
            totalPrice.visibility = View.VISIBLE
            makeOrder.visibility = View.VISIBLE
            orderRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            orderRecycler.adapter = OrderRecyclerAdapter(dishesList, pref, binding.orderPrice)
            makeOrder.setOnClickListener {
                foodApi = (application as? App)?.foodApi!!
                compositeDisposable.add(foodApi.sendOrderRequest(
                    OrderRequest(
                        adress,
                        getCurrentDate(),
                        pref?.getList<DishesOrder>("DishesOrder") as MutableList<DishesOrder>
                    )
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        SaveData(mutableListOf())
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }, {

                    }))
            }
        }

        val homeBtn = binding.orderHome
        homeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val historyBtn = binding.orderHistory
        historyBtn.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
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

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
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

    fun getCurrentDate():String{
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")
        return sdf.format(Date())
    }
}