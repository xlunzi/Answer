package cn.xlunzi.answer.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cn.xlunzi.answer.R
import cn.xlunzi.answer.data.AnswerDao
import cn.xlunzi.answer.data.AnswerDatabase
import kotlin.random.Random

/**
 * @author xlunzi
 * @date 2020/6/24 11:30
 */
class AnswerViewModel(application: Application) :
    AndroidViewModel(application) {

    private val dao: AnswerDao = AnswerDatabase.getInstance(application).answerDao()

    var size: Int = -1

    val liveDataSize = dao.getSize()

    val initTip = "数据初始化未完成，请稍后再试"
    val showResult = "显示答案"
    val isPrepare = "准备"
    val focusYourProblem = "请专注您的问题"

    fun tip(): String {
        val stringArray =
            getApplication<Application>().resources.getStringArray(R.array.book_review)
        return stringArray[Random.nextInt(stringArray.size)]
    }

    suspend fun randomValue(): String {
        if (size == -1) {
            return "size is not init"
        }
        if (size <= 0) {
            return "bound must be positive"
        }
        val position = Random.nextInt(size) + 1
        return dao.getInfoByKey(position)?.info ?: "Sorry, please try again"
    }

}