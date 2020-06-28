package cn.xlunzi.answer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

/**
 * @author xlunzi
 * @date 2020/6/24 14:30
 */
@Database(entities = [AnswerInfo::class], version = 1, exportSchema = false)
abstract class AnswerDatabase : RoomDatabase() {

    abstract fun answerDao(): AnswerDao

    companion object {

        @Volatile
        private var instance: AnswerDatabase? = null

        fun getInstance(context: Context): AnswerDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AnswerDatabase {
            return Room.databaseBuilder(context, AnswerDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .build()
        }
    }
}