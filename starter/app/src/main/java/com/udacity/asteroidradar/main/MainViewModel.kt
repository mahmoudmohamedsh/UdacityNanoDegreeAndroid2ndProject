package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.squareup.moshi.Json
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsterApi
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepoitory
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepo = AsteroidRepoitory(database)

    private val _navigateToDetails = MutableLiveData<Asteroid>()
    val navigateToDetails: LiveData<Asteroid>
    get() = _navigateToDetails

    fun onAsteroidClicked(aster:Asteroid){
        _navigateToDetails.value = aster
    }
    fun onAsteroidNavigated(){
        _navigateToDetails.value = null
    }

    private val _img = MutableLiveData<PictureOfDay>()
    val img: LiveData<PictureOfDay>
        get() = _img

    private val _imgStatus = MutableLiveData<String>()
    val imgStatus : LiveData<String>
        get() = _imgStatus

    init {
        getImageOfTodayFromNetwork()
        viewModelScope.launch {
            asteroidRepo.refreshAsteroid()
        }

    }
    //  hold the db live data
    //  to observe inside the fragment
    val asteroidList = asteroidRepo.asteroid




    private fun getImageOfTodayFromNetwork() {
        viewModelScope.launch {
            try {
                //?start_date=2015-09-07&end_date=2015-09-08&api_key=DEMO_KEY/
                _img.value = AsterApi.retrofitService.getTodayImage(
                    param = "4bShsvdAvynLuHy3G95mGZ2c5pO5XxtlwVeNEjnW"
                )
                _imgStatus.value = "success"
            } catch (e: Exception) {
                _img.value = null
                Log.e("error",e.toString())
                _imgStatus.value = e.toString()
            }
        }

    }
}

