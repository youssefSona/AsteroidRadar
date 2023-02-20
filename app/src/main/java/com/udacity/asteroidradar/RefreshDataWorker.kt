package com.udacity.asteroidradar

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun doWork(): Result {
        val database = AsteroidDB.getDatabase(applicationContext)
        val repository = AsteroidsRepo(database)
        return try {
            repository.refreshData()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }

    }
}
