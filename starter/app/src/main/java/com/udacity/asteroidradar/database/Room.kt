package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AstroidDao{
    @Query("select * from DatabaseAsteroid order by closeApproachDate ")
     fun getAsteroid(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from DatabaseAsteroid WHERE closeApproachDate = :param ")
     fun getTodayAsteroid(param : String): LiveData<List<DatabaseAsteroid>>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll(vararg asteroids: DatabaseAsteroid)
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AstroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidDatabase::class.java,
                "videos").build()
        }
    }
    return INSTANCE
}