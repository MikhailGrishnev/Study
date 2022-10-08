package com.belkinapps.wsrfood

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.belkinapps.wsrfood.data.remote.FoodApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class App: Application() {

    lateinit var foodApi: FoodApi
    var pref: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()

        configureRetrofit()
    }

    fun configureRetrofit() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        val jwtToken = pref?.getInt("jwtToken", 0)!!

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $jwtToken")
                    .build()
                return@addInterceptor chain.proceed(request)
            }
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://food.madskill.ru")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        foodApi = retrofit.create(FoodApi::class.java)
    }
}