package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidDB
import com.udacity.asteroidradar.AsteroidsRepo
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var database = AsteroidDB.getDatabase(application)
    private val repository = AsteroidsRepo(database)

    private val dataDate = MutableLiveData(DataDate.ALL)

    @RequiresApi(Build.VERSION_CODES.N)
    val asteroids: LiveData<List<Asteroid>> = Transformations.switchMap(dataDate) { dataDate ->
        when (dataDate!!) {
            DataDate.TODAY -> repository.dataToday
            DataDate.WEEK -> repository.dataWeek
            DataDate.ALL -> repository.dataAll
        }
    }

    fun setDataDate(d : DataDate){
        dataDate.value = d
    }

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    init {
        refreshAsteroids()
        getPictureOfDay()
    }

    @RequiresApi(Build.VERSION_CODES.N)
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


enum class DataDate {
    WEEK,
    ALL,
    TODAY
}