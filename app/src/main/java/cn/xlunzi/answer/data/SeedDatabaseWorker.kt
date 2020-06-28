package cn.xlunzi.answer.data

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 * @author xlunzi
 * @date 2020/6/24 14:53
 */
class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        withContext(Dispatchers.IO) {
            try {
                applicationContext.assets.open(PLANT_DATA_FILENAME).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val plantType = object : TypeToken<List<AnswerInfo>>() {}.type
                        val plantList: List<AnswerInfo> = try {
                            Gson().fromJson(jsonReader, plantType)
                        } catch (e: Exception) {
                            listOf()
                        }

                        val database = AnswerDatabase.getInstance(applicationContext)
                        database.answerDao().insertAll(plantList)
                        Log.d(TAG, "success seeding database, size = ${plantList.size}")
                        Result.success()
                    }
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Error seeding database", ex)
                Result.failure()
            }
        }
    }

    companion object {
        private val TAG = SeedDatabaseWorker::class.java.simpleName
    }

}