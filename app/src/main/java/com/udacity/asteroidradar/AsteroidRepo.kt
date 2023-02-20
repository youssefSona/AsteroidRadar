package com.udacity.asteroidradar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepo(private val database: AsteroidDB) {



    val dataAll: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidData()) {
            it.asAsteroids()
        }

    @RequiresApi(Build.VERSION_CODES.N)
    val dataToday: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidDataToday(getNextSevenDaysFormattedDates()[0])) {
            it.asAsteroids()
        }

    @RequiresApi(Build.VERSION_CODES.N)
    val dataWeek: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidDataWeek(getNextSevenDaysFormattedDates()[0])) {
            it.asAsteroids()
        }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun refreshData() {
        withContext(Dispatchers.IO) {
            val response = AsteroidApi.retrofitService.getAsteroids()
            val jasonOpj = JSONObject(response)
            val webData = parseAsteroidsJsonResult(jasonOpj)
            database.asteroidDao.insertAll(webData.asEntinty())
        }

    }

    suspend fun getPictureOfDay(): PictureOfDay {
        lateinit var pictureOfDay: PictureOfDay
        withContext(Dispatchers.IO) {
            pictureOfDay = AsteroidApi.retrofitService.getImage()
        }
        return pictureOfDay
    }

}
