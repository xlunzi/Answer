package cn.xlunzi.answer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.xlunzi.answer.databinding.FragmentOtherBinding

/**
 * @author xlunzi
 * @date 2020/6/29 10:16
 */
class OtherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentOtherBinding =
            FragmentOtherBinding.inflate(inflater, container, false)
        return binding.root
    }
}