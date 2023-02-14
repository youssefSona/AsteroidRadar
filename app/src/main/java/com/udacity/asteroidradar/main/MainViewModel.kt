package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.AsteroidDB
import com.udacity.asteroidradar.AsteroidsRepo
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var database = AsteroidDB.getDatabase(application)
    private val repository = AsteroidsRepo(database)
    val asteroids = repository.data

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    init {
        refreshAsteroids()
        getPictureOfDay()
    }

    private fun refreshAsteroids() {
        viewModelScope.launch {
            try {
                repository.refreshData()
                Log.e("MyTag", "Data Refreshed")
            } catch (_: Exception) {
            }
        }
    }// end of fun

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                _pictureOfDay.value = repository.getPictureOfDay()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    } // end of fun

}
