package com.udacity.asteroidradar

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface AsteroidDao {
    @Query("select * from asteroidentity")
    fun getAsteroidData(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroidentity WHERE closeApproachDate = :date")
    fun getAsteroidDataToday(date: String): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroidentity")
    fun getAsteroidDataWeek(): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(entitys: List<AsteroidEntity>)

    @Query("DELETE FROM asteroidentity")
    suspend fun clear()
}

@Database(entities = [AsteroidEntity::class], version = 1, exportSchema = false)
abstract class AsteroidDB : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: AsteroidDB
        fun getDatabase(context: Context): AsteroidDB {
            synchronized(AsteroidDB::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDB::class.java,
                        "dataName"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}
