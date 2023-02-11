package com.udacity.asteroidradar.api

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.nasa.gov/"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface AsterApiService {

    @GET("neo/rest/v1/feed/")
    //?start_date=2015-09-07&end_date=2015-09-08&api_key=DEMO_KEY/
    suspend fun getAster(
        @Query("start_date") startDate: String,
        @Query("end_date") endData: String,
        @Query("api_key") apiKey: String,
    ): String


    // ?api_key=KEGQwmIBgQLG925CxttHD7sZ7gl0McUuHP4ftZBC
    @GET("planetary/apod/")
    suspend fun getTodayImage(@Query("api_key") param: String): PictureOfDay
}

object AsterApi {
    val retrofitService: AsterApiService by lazy {
        retrofit.create(AsterApiService::class.java)
    }
}

