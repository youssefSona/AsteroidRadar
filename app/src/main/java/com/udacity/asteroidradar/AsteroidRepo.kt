package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepo(val database: AsteroidDB) {
    val data: LiveData<List<Asteroid>> =
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