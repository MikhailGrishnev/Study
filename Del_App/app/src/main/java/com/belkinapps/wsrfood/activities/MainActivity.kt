package com.belkinapps.wsrfood.activities

import GridSpacingItemDecoration
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.belkinapps.wsrfood.R
import com.belkinapps.wsrfood.ViewPagerAdapter
import com.belkinapps.wsrfood.adapters.MenuPagerAdapter
import com.belkinapps.wsrfood.adapters.MenuRecyclerAdapter
import com.belkinapps.wsrfood.data.responses.Item
import com.belkinapps.wsrfood.databinding.ActivityMainBinding
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    var pref: SharedPreferences? = null
    var isLogged = false
    private val tabTitles: Array<String> = arrayOf(
        "Foods",
        "Drinks",
        "Snacks",
        "Sauce"
    )
    private lateinit var binding: ActivityMainBinding
    var dishesList: MutableList<Item> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        dishesList = pref?.getList<Item>("menu") as MutableList<Item>
        var adress = pref?.getString("adress", "Выберите адрес доставки")
        if (adress == "") {
            adress = "Выберите адрес доставки"
        }
        val adressField = binding.adressField
        adressField.hint = adress
        val tabLayout = binding.tabLayout
        val view_pager2: ViewPager2 = binding.mainPager
        isLogged = pref?.getBoolean("isLogged", false)!!
        if (isLogged) {
            val view = binding.root
            setContentView(view)
            view_pager2.adapter = MenuPagerAdapter(dishesList, pref)
            TabLayoutMediator(tabLayout, view_pager2) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()
        } else {
            val intent = Intent(this, OBSActivity::class.java)
            startActivity(intent)
        }

        val main_user = binding.mainUser
        main_user.setOnClickListener {
            isLogged = false
            SaveState(isLogged)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val results_text = binding.resultsText
        val search_btn = binding.searchBtn
        val search_in_bar = binding.searchInBar
        val close_btn = binding.closeSearchBtn
        val searchBar = binding.searchBar
        val search_menu = binding.searchMenu

        search_btn.setOnClickListener {
            searchBar.visibility = View.VISIBLE
            search_in_bar.visibility = View.VISIBLE
            close_btn.visibility = View.VISIBLE
        }

        close_btn.setOnClickListener {
            searchBar.visibility = View.INVISIBLE
            search_in_bar.visibility = View.INVISIBLE
            close_btn.visibility = View.INVISIBLE
            tabLayout.visibility = View.VISIBLE
            results_text.visibility = View.INVISIBLE
            view_pager2.visibility = View.VISIBLE
            searchBar.text.clear()
        }
        var search_menu_adapter = MenuRecyclerAdapter(dishesList, pref)
        search_menu.layoutManager = GridLayoutManager(applicationContext, 2)
        search_menu.addItemDecoration(GridSpacingItemDecoration(2, 35, true, 0))
        search_menu.adapter = search_menu_adapter

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                search_menu_adapter.setFilteredList(filterList(searchBar.text.toString()))
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (searchBar.text.toString() == "") {
                    tabLayout.visibility = View.VISIBLE
                    results_text.visibility = View.INVISIBLE
                    search_menu.visibility = View.INVISIBLE
                    view_pager2.visibility = View.VISIBLE
                }
                else {
                    tabLayout.visibility = View.INVISIBLE
                    results_text.visibility = View.VISIBLE
                    search_menu.visibility = View.VISIBLE
                    view_pager2.visibility = View.INVISIBLE

                }
            }

        })

        val orderBtn = binding.mainCart
        orderBtn.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }

        val historyBtn = binding.mainHistory
        historyBtn.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        adressField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                SaveAdress(adressField.text.toString())
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                SaveAdress(adressField.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                SaveAdress(adressField.text.toString())
            }

        })

    }

    @SuppressLint("CheckResult")
    fun filterList(text: String): MutableList<Item> {
        var filteredList: MutableList<Item> = mutableListOf()
        for (item: Item in dishesList) {
            if (item.nameDish.toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item)
            }
        }

        Completable
            .timer(50, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io()) // where the work should be done
            .observeOn(AndroidSchedulers.mainThread()) // where the data stream should be delivered
            .subscribe({
                if (filteredList.isEmpty()) {
                    Toast.makeText(applicationContext, "Ничего не найдено", Toast.LENGTH_SHORT).show()
                }
            }, {
                Toast.makeText(applicationContext, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
            })

        return filteredList
    }

    inline fun <reified T> SharedPreferences.getList(spListKey: String): MutableList<T> {
        val listJson = getString(spListKey, "")
        if (!listJson.isNullOrBlank()) {
            val type = object : TypeToken<MutableList<T>>() {}.type
            return Gson().fromJson(listJson, type)
        }
        return mutableListOf()
    }

    fun SaveAdress(adress: String) {
        val editor = pref?.edit()
        editor?.putString("adress", adress)
        editor?.apply()
    }

    fun SaveState(state: Boolean) {
        val editor = pref?.edit()
        editor?.putBoolean("isLogged", state)
        editor?.apply()
    }
}
