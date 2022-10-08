package com.belkinapps.wsrfood.activities

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.belkinapps.wsrfood.ViewPagerAdapter
import com.belkinapps.wsrfood.databinding.OnBoardingScreenBinding
import com.google.android.material.tabs.TabLayoutMediator

class OBSActivity : AppCompatActivity() {

    private lateinit var binding: OnBoardingScreenBinding
    var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        binding = OnBoardingScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val view_pager2: ViewPager2 = binding.pager
        val tabLayout = binding.tabLayout
        val connectionManager: ConnectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectionManager.activeNetworkInfo
        val isConnected = activeNetwork?.isConnectedOrConnecting == true
        view_pager2.adapter = ViewPagerAdapter(isConnected, pref)
        TabLayoutMediator(tabLayout, view_pager2) { tab, position ->
            //Some implementation...
        }.attach()
    }
}