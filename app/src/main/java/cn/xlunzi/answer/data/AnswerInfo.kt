package cn.xlunzi.answer.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author xlunzi
 * @date 2020/6/24 14:34
 */
@Entity(tableName = "answer_info")
data class AnswerInfo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "position")
    var position: Int = 0,
    @ColumnInfo(name = "info")
    var info: String = ""
)