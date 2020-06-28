package cn.xlunzi.answer.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cn.xlunzi.answer.R
import cn.xlunzi.answer.model.AnswerViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import kotlin.random.Random

/**
 * @author xlunzi
 * @date 2020/6/28 19:01
 */
class MainFragment : Fragment() {

    private var isPrepared: Boolean = true
    private var job: Job? = null
    private var maxProgress = 1000
    private var randomColor: Int = -1

    private val viewModel: AnswerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showStart()
        progressBar.max = maxProgress

        viewModel.liveDataSize.observe(viewLifecycleOwner, Observer {
            viewModel.size = it ?: -1
        })

        btnStart.setOnClickListener {
            if (viewModel.size <= 0) {
                Toast.makeText(requireContext(), viewModel.initTip, Toast.LENGTH_SHORT).show()
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