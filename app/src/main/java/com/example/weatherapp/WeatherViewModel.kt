package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.Constant
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.RetrofitInstance
import com.example.weatherapp.api.weatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel()
{
    private val weatherApi =RetrofitInstance.weatherApi
    private val _weatherResult =MutableLiveData<NetworkResponse<weatherModel>>()
    val weatherResult : LiveData<NetworkResponse<weatherModel>> =_weatherResult

    fun getCity(city : String)
    {
        viewModelScope.launch {
            _weatherResult.value =NetworkResponse.Loading
            try {
                val response = weatherApi.getWeather(Constant.apikey, city)

                if(response.isSuccessful)
                {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                }
                else{
                    _weatherResult.value = NetworkResponse.Error("Invalid Location")
                }
            }
            catch (e :Exception)
            {
                _weatherResult.value = NetworkResponse.Error("Failed to load Data")
            }
        }
    }
}