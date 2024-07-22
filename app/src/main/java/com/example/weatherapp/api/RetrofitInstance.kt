package com.example.weatherapp.api

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance
{


    fun getInstance() : Retrofit
    {
        return Retrofit.Builder()
            .baseUrl(Constant.burl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val weatherApi : WeatherApi = getInstance().create(WeatherApi::class.java)
}