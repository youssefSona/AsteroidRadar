package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequests {
    @GET("planetary/apod")
    suspend fun getImage(
        @Query("api_key") apiKey: String = API_KEY
    ): PictureOfDay

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String = "",
        @Query("end_date") endDate: String = "",
        @Query("api_key") apiKey: String = API_KEY
    ): String
}

object AsteroidApi {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(Constants.BASE_URL)
        .build()

    val retrofitService: ApiRequests by lazy {
        retrofit.create(ApiRequests::class.java)
    }
}
