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
import com.belkinapps.wsrfood.adapters.HistoryItemsRecycler
import com.belkinapps.wsrfood.data.remote.FoodApi
import com.belkinapps.wsrfood.data.requests.DishesOrder
import com.belkinapps.wsrfood.data.requests.OrderRequest
import com.belkinapps.wsrfood.data.responses.Item
import com.belkinapps.wsrfood.databinding.ActivityHistoryBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val compositeDisposable = CompositeDisposable()
    lateinit var foodApi: FoodApi
    var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        val noHistoryImage = binding.noHistory
        val noOrdersText = binding.noOrdersText
        var history: List<OrderRequest>
        val historyItemsRecycler = binding.historyItemsRecycler
        val dishesList: MutableList<Item> = pref?.getList<Item>("menu") as MutableList<Item>

        foodApi = (application as? App)?.foodApi!!
        compositeDisposable.add(foodApi.sendHistoryRequest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!it.isEmpty()) {
                    noHistoryImage.visibility = View.GONE
                    noOrdersText.visibility = View.GONE
                    historyItemsRecycler.visibility = View.VISIBLE
                    history = it
                    SaveData(history)
                    historyItemsRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                    historyItemsRecycler.adapter = HistoryItemsRecycler(history, dishesList)
                }

            }, {

            }))

        val homeBtn = binding.historyHome
        homeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val orderBtn = binding.historyCart
        orderBtn.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
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

    fun SaveData(historyList: List<OrderRequest>) {
        val editorList = pref
        editorList?.putList("HistoryList", historyList)
        val editor = pref?.edit()
        editor?.apply()
    }

    fun <T> SharedPreferences.putList(spListKey: String, list: List<T>) {
        val listJson = Gson().toJson(list)
        edit {
            putString(spListKey, listJson)
        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}