package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepo(private val database: AsteroidDB) {

    val dataAll: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidData()) {
            it.asAsteroids()
        }

    val dataToday: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidDataToday("2023-02-19")) {
            it.asAsteroids()
        }

    val dataWeek: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidData()) {
            it.asAsteroids()
        }

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
