package com.example.weatherapp

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.weatherapp.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get current date, time, and day
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        val currentDate = dateFormat.format(calendar.time)
        val currentDay = dayFormat.format(calendar.time)
        val currentTime = timeFormat.format(calendar.time)

        // Set current date and day to TextViews
        binding.date.text = currentDate
        binding.day.text = currentDay

        // Set up SearchView listener
        binding.searchView.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    fetchWeatherData(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text change if needed
                return false
            }
        })

        // Initial weather data fetch (for default city, e.g., Bijnor)
        fetchWeatherData("Bijnor")

        // Fetch weather data and observe changes
    }

    private fun fetchWeatherData(city: String) {
        viewModel.getWeather(city, "faa7c29a0342045a6bd3bafd5bd71408", "metric")
            .observe(this, Observer { weatherData ->
                if (weatherData != null) {
                    // Set temperature
                    val temperature = weatherData.main.temp
                    binding.temperature.text = "$temperature °C"

                    // Set location (if needed)
                    val location = weatherData.name
                    binding.location.text = location

                    // Set weather description
                    val weatherDescription = weatherData.weather.firstOrNull()?.main ?: "Unknown"
                    binding.weather.text = weatherDescription

                    changeBackgroundImageView(weatherDescription)

                    // Set humidity
                    val humidity = weatherData.main.humidity
                    binding.humidity.text = "$humidity%"

                    // Set wind speed
                    val windSpeed = weatherData.wind.speed
                    binding.wind.text = "$windSpeed m/s"


                } else {
                    Log.d("WeatherData", "Failed to fetch weather data")
                }
            })
    }

    private fun changeBackgroundImageView(weatherDescription: String) {

        when (weatherDescription) {
            "Clear Sky", "Sunny", "Clear" -> {
                binding.main.setBackgroundResource(R.drawable.sunny_image)
                binding.imageView2.setBackgroundResource(R.drawable.sun_icon)
            }

            "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy", "Haze" -> {
                binding.main.setBackgroundResource(R.drawable.cloud)
                binding.imageView2.setBackgroundResource(R.drawable.cloud_icon)
            }

            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain", "Rain" -> {
                binding.main.setBackgroundResource(R.drawable.rain_image)
                binding.imageView2.setBackgroundResource(R.drawable.rain_icon)
            }

            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard" -> {
                binding.main.setBackgroundResource(R.drawable.snow)
                binding.imageView2.setBackgroundResource(R.drawable.snow_icon)
            }

            else -> {
                binding.main.setBackgroundResource(R.drawable.sunny_image)
                binding.imageView2.setBackgroundResource(R.drawable.sun_icon)
            }

        }
    }
}


