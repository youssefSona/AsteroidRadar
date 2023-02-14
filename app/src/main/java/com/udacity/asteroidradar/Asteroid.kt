package com.udacity.asteroidradar

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Asteroid(
    val id: Long,
//    @Json(name = "name")
    val codename: String,
//    @Json(name = "close_approach_date")
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
//    @Json(name = "is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean,
//    val element_count: Int,
//    val near_earth_objects: Map<String, Map<String, Any>>
)  : Parcelable
