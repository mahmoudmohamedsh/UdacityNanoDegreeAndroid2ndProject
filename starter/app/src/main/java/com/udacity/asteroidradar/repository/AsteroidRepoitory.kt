package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsterApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.asDataBaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepoitory(private val database : AsteroidDatabase) {

    val asteroid: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroid()) {
            it.asDomainModel()
        }


    suspend fun refreshAsteroid(){
        withContext(Dispatchers.IO){
            val calendar = Calendar.getInstance()
            var currentTime = calendar.time
            var dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            var now = dateFormat.format(currentTime)

            calendar.add(Calendar.DAY_OF_YEAR, 7)
            currentTime = calendar.time
            dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            var nextSevenDay = dateFormat.format(currentTime)

            //?start_date=2015-09-07&end_date=2015-09-08&api_key=DEMO_KEY/
            var res : String = AsterApi.retrofitService.getAster(
                startDate = now.toString(),
                endData = nextSevenDay,
                apiKey = "4bShsvdAvynLuHy3G95mGZ2c5pO5XxtlwVeNEjnW"
            )

            var jsonList = JSONObject(res)
            var list : List<Asteroid> = parseAsteroidsJsonResult(jsonList).toList()
            database.asteroidDao.insertAll(*list.asDataBaseModel())
        }
    }


}