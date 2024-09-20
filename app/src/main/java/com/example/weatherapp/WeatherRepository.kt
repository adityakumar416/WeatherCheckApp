package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRepository {

    val weatherData = MutableLiveData<WeatherData?>()


    fun fetchWeather(city: String, apiKey: String, units: String): LiveData<WeatherData?> {
        val apiInterface = RetrofitInstance.api.getWeather(city, apiKey, units)

        apiInterface.enqueue(object : Callback<WeatherData?> {
            override fun onResponse(call: Call<WeatherData?>, response: Response<WeatherData?>) {
                if (response.isSuccessful) {
                    weatherData.postValue(response.body())
                } else {
                    weatherData.postValue(null)
                }
            }

            override fun onFailure(call: Call<WeatherData?>, t: Throwable) {
                weatherData.postValue(null)
            }
        })

        return weatherData
    }
}
