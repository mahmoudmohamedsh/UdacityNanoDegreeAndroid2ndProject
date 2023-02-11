package com.udacity.asteroidradar.api

import android.os.Build
import androidx.annotation.RequiresApi
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun parseAsteroidsJsonResult(jsonResult: JSONObject): ArrayList<Asteroid> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

    val asteroidList = ArrayList<Asteroid>()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {
        if (nearEarthObjectsJson.has(formattedDate)) {
            val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

            for (i in 0 until dateAsteroidJsonArray.length()) {
                val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
                val id = asteroidJson.getLong("id")
                val codename = asteroidJson.getString("name")
                val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
                val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                    .getJSONObject("kilometers").getDouble("estimated_diameter_max")

                val closeApproachData = asteroidJson
                    .getJSONArray("close_approach_data").getJSONObject(0)
                val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                    .getDouble("kilometers_per_second")
                val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                    .getDouble("astronomical")
                val isPotentiallyHazardous = asteroidJson
                    .getBoolean("is_potentially_hazardous_asteroid")

                val asteroid = Asteroid(id, codename, formattedDate, absoluteMagnitude,
                    estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous)
                asteroidList.add(asteroid)
            }
        }
    }

    return asteroidList
}


 fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}
/*
*
* {"links":{"self":"http://api.nasa.gov/neo/rest/v1/neo/2465633?api_key=DEMO_KEY"},
* "id":"2465633",
* "neo_reference_id":"2465633",
* "name":"465633 (2009 JR5)",
* "nasa_jpl_url":"http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2465633",
* "absolute_magnitude_h":20.48,
* "estimated_diameter":{
*   "kilometers":{"estimated_diameter_min":0.2130860292,"estimated_diameter_max":0.4764748465},
*   "meters":{"estimated_diameter_min":213.0860292484,"estimated_diameter_max":476.474846455},
*   "miles":{"estimated_diameter_min":0.1324054791,"estimated_diameter_max":0.2960676518},
*   "feet":{"estimated_diameter_min":699.1011681995,"estimated_diameter_max":1563.2377352435}
* },
* "is_potentially_hazardous_asteroid":true,
* "close_approach_data":[
    *   {"close_approach_date":"2015-09-08",
    * "close_approach_date_full":"2015-Sep-08 20:28",
    * "epoch_date_close_approach":1441744080000,
    * "relative_velocity":{"kilometers_per_second":"18.1279370671","kilometers_per_hour":"65260.5734417159","miles_per_hour":"40550.3824254928"},
    * "miss_distance":{"astronomical":"0.3027469748","lunar":"117.7685731972","kilometers":"45290302.579023676","miles":"28142089.0565956888"},
    * "orbiting_body":"Earth"}
* ],
* "is_sentry_object":false
* }
* */