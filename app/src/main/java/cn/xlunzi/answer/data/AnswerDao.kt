package cn.xlunzi.answer.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * @author xlunzi
 * @date 2020/6/24 14:51
 */
@Dao
interface AnswerDao {

    @Query("SELECT * FROM ANSWER_INFO")
    fun queryInfoList(): LiveData<List<AnswerInfo>>

    @Query("SELECT count(*) FROM ANSWER_INFO")
    fun getSize(): LiveData<Int?>

    @Query("SELECT * FROM ANSWER_INFO WHERE position =:position")
    suspend fun getInfoByKey(position: Int): AnswerInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(infoList: List<AnswerInfo>)

}