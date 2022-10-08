package com.belkinapps.wsrfood.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.widget.WearableLinearLayoutManager
import com.belkinapps.wsrfood.App
import com.belkinapps.wsrfood.adapters.WearableRecyclerAdapter
import com.belkinapps.wsrfood.data.remote.FoodApi
import com.belkinapps.wsrfood.data.requests.LoginRequest
import com.belkinapps.wsrfood.data.responses.Item
import com.belkinapps.wsrfood.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    var pref: SharedPreferences? = null
    var isLogged = false
    private val compositeDisposable = CompositeDisposable()
    lateinit var foodApi: FoodApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        isLogged = pref?.getBoolean("isLogged", false)!!

        if (isLogged) {
            val view = binding.root
            setContentView(view)
        } else {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        val dishesList = pref?.getList<Item>("menu") as MutableList<Item>
        foodApi = (application as? App)?.foodApi!!
        compositeDisposable.add(foodApi.sendCouriersRequest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val orderRecycler = binding.orderRecycler
                orderRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                orderRecycler.adapter = WearableRecyclerAdapter(dishesList, it.dishes)
                val adress = binding.adress
                adress.text = it.address
                val date = binding.date
                date.text = it.date
            }, {

            }))

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
}