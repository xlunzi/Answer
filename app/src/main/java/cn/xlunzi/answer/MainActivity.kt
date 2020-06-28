package cn.xlunzi.answer

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import cn.xlunzi.answer.model.AnswerViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var isPrepared: Boolean = true
    private var job: Job? = null
    private var maxProgress = 1000
    private var randomColor: Int = -1

    private val viewModel: AnswerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showStart()
        progressBar.max = maxProgress

        viewModel.liveDataSize.observe(this, Observer {
            viewModel.size = it ?: -1
        })

        btnStart.setOnClickListener {
            if (viewModel.size <= 0) {
                Toast.makeText(this, viewModel.initTip, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (isPrepared) {
                showResult()
            } else {
                if (job != null && job!!.isActive) {
                    job!!.cancel()
                }
                isPrepared()
            }
        }
    }

    private fun showStart() {
        tvAnswer.text = viewModel.tip()
        tvAnswer.setTextColor(Color.DKGRAY)
        tvAnswer.setBackgroundColor(Color.TRANSPARENT)
        progressBar.visibility = View.INVISIBLE
        randomColor = -1
    }

    private fun isPrepared() {
        btnStart.text = viewModel.showResult
        isPrepared = true
        showStart()
    }

    private fun showResult() {
        btnStart.text = viewModel.isPrepare
        isPrepared = false

        job = GlobalScope.launch {
            withContext(Dispatchers.Main) {
                setText(tvAnswer, 0)
            }
            val answer = viewModel.randomValue()
            withContext(Dispatchers.Main) {
                tvAnswer.text = answer
                tvAnswer.setTextColor(Color.parseColor("#FFFFBB33"))
                tvAnswer.setBackgroundColor(Color.parseColor("#6600FFFF"))
            }
        }
    }

    private suspend fun setText(tv: TextView, text: Int) {
        progressBar.visibility = View.VISIBLE
        tv.text = viewModel.focusYourProblem
        if (randomColor == -1) {
            randomColor = 0xFF000000.toInt() or Random.nextInt(0x00FFFFFF)
        } else {
            randomColor += 1
        }
        tv.setTextColor(randomColor)
        progressBar.progress = text
        if (text >= maxProgress) {
            progressBar.visibility = View.INVISIBLE
            return
        }
        delay(10)
        setText(tv, text + 1)
    }

}