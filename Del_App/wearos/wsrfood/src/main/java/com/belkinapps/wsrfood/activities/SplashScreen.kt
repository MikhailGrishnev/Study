package com.belkinapps.wsrfood.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.edit
import com.belkinapps.wsrfood.App
import com.belkinapps.wsrfood.data.remote.FoodApi
import com.belkinapps.wsrfood.data.requests.DishesOrder
import com.belkinapps.wsrfood.data.responses.Item
import com.belkinapps.wsrfood.databinding.ActivitySplashScreenBinding
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SplashScreen : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var binding: ActivitySplashScreenBinding
    lateinit var foodApi: FoodApi
    var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        var versions: List<String> = listOf()
        var version = "1.01"
        val empty_list = listOf<Item>()
        version = pref?.getString("version", "1.01")!!
        val mapper = jacksonObjectMapper()
        val menu_version101_string = """
        [{"dishId":"5","category":"Foods","nameDish":"\u042d\u0441\u043a\u0430\u043b\u043e\u043f \u0441 \u043e\u0442\u0432\u0430\u0440\u043d\u044b\u043c \u043a\u0430\u0440\u0442\u043e\u0444\u0435\u043b\u0435\u043c","price":"1720","icon":"esca.jpeg","version":"1.01"},{"dishId":"6","category":"Foods","nameDish":"\u041a\u0430\u0440\u0442\u043e\u0444\u0435\u043b\u044c \u0441 \u043c\u044f\u0441\u043e\u043c","price":"920","icon":"meat.jpg","version":"1.01"},{"dishId":"8","category":"Foods","nameDish":"\u041a\u0430\u0440\u0442\u043e\u0444\u0435\u043b\u044c \u0444\u0440\u0438 \u0441 \u043a\u043e\u0442\u043b\u0435\u0442\u0430\u043c\u0438","price":"480","icon":"noice.jpeg","version":"1.01"},{"dishId":"9","category":"Foods","nameDish":"\u0422\u0430\u0440\u0435\u043b\u043a\u0430 \u0441 \u0442\u0435\u0444\u0442\u0435\u043b\u044f\u043c\u0438","price":"320","icon":"teft.jpg","version":"1.01"},{"dishId":"10","category":"Foods","nameDish":"\u041b\u043e\u0441\u043e\u0441\u044c \u0436\u0430\u0440\u0435\u043d\u044b\u0439","price":"980","icon":"losos.jpg","version":"1.01"},{"dishId":"12","category":"Foods","nameDish":"Wok \u0441 \u043a\u0443\u0440\u0438\u0446\u0435\u0439","price":"540","icon":"wok.jpg","version":"1.01"},{"dishId":"13","category":"Drinks","nameDish":"\u0413\u0440\u0430\u043d\u0430\u0442","price":"280","icon":"granat.jpg","version":"1.01"},{"dishId":"14","category":"Drinks","nameDish":"\u041b\u0438\u043c\u043e\u043d\u0430\u0434 \"\u041c\u0430\u043b\u0438\u043d\u0430\"","price":"320","icon":"malina.jpg","version":"1.01"},{"dishId":"15","category":"Drinks","nameDish":"\u041b\u0438\u043c\u043e\u043d\u0430\u0434 \u043c\u0430\u0440\u0430\u043a\u0443\u0439\u044f","price":"320","icon":"marak.jpg","version":"1.01"},{"dishId":"16","category":"Drinks","nameDish":" \u041a\u043e\u043a\u0430 \u043a\u043e\u043b\u0430","price":"420","icon":"cola.jpg","version":"1.01"},{"dishId":"17","category":"Drinks","nameDish":"\u041b\u0438\u043c\u043e\u043d\u0430\u0434 \"\u041a\u043b\u0443\u0431\u043d\u0438\u043a\u0430\"","price":"502","icon":"klub.png","version":"1.01"},{"dishId":"18","category":"Drinks","nameDish":"\u041b\u0438\u043c\u043e\u043d\u0430 \"\u0412\u0438\u0448\u043d\u044f\"","price":"890","icon":"vish.jpeg","version":"1.01"},{"dishId":"19","category":"Snacks","nameDish":"\u0420\u043e\u0437\u043e\u0447\u043a\u0438 \u0441 \u043a\u0430\u043b\u044c\u043c\u0430\u0440\u043e\u043c","price":"1990","icon":"kalmar.jpg","version":"1.01"},{"dishId":"20","category":"Snacks","nameDish":"\u041a\u043e\u0440\u0437\u0438\u043d\u043a\u0438 \u0441 \u0441\u0430\u043b\u0430\u0442\u0440\u043e\u043c","price":"1150","icon":"salat.jpg","version":"1.01"},{"dishId":"21","category":"Snacks","nameDish":"\u041c\u044f\u0441\u043d\u0430\u044f \u043d\u0430\u0440\u0435\u0437\u043a\u0430","price":"460","icon":"myaso.jpg","version":"1.01"},{"dishId":"22","category":"Snacks","nameDish":"\u041a\u043e\u043b\u0431\u0430\u0441\u043d\u0430\u044f \u043d\u0430\u0440\u0435\u0437\u043a\u0430","price":"320","icon":"kolbasa.jpg","version":"1.01"},{"dishId":"23","category":"Snacks","nameDish":"\u0421\u0435\u043b\u0435\u0434\u043a\u0430 \u0441 \u043a\u0430\u0440\u0442\u043e\u0448\u043a\u043e\u0439","price":"870","icon":"seledka.jpg","version":"1.01"},{"dishId":"24","category":"Sauce","nameDish":"\u0421\u043b\u0430\u0434\u043a\u0438\u0439 \u0447\u0438\u043b\u0438","price":"5","icon":"chili.png","version":"1.01"},{"dishId":"25","category":"Sauce","nameDish":"\u0422\u043e\u043c\u0430\u0442\u043d\u044b\u0439","price":"2","icon":"tomat.jpg","version":"1.01"},{"dishId":"26","category":"Sauce","nameDish":"\u0421\u043f\u0430\u0439\u0441\u0438","price":"38","icon":"spasi.png","version":"1.01"}]
    """
        var dishesList: MutableList<Item> = mapper.readValue(menu_version101_string)
        if (pref?.getList<Item>("menu") != empty_list) {
            dishesList = pref?.getList<Item>("menu") as MutableList<Item>
        }
        foodApi = (application as? App)?.foodApi!!
        compositeDisposable.add(foodApi.sendVersionRequest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                versions = it.version
                for (i in versions.indexOf(version)+1..versions.size-1) {
                    compositeDisposable.add(foodApi.sendDishesRequest(versions[i])
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            dishesList.addAll(it)
                        }, {

                        }))
                }
                version = versions[versions.size-1]
            }, {

            }))

        Handler(Looper.getMainLooper()).postDelayed({
            SaveData(version, dishesList)
            var intent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    fun SaveData(version: String, menu: MutableList<Item>) {
        val editorList = pref
        editorList?.putList("menu", menu)
        val editor = pref?.edit()
        editor?.putString("version", version)
        editor?.apply()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    inline fun <reified T> SharedPreferences.addItemToList(spListKey: String, item: T) {
        val savedList = getList<T>(spListKey).toMutableList()
        savedList.add(item)
        val listJson = Gson().toJson(savedList)
        edit { putString(spListKey, listJson) }
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