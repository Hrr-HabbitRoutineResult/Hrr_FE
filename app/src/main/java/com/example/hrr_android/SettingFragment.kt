package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.example.hrr_android.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ViewBinding 초기화
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        val switchCompat = binding.switchCompat

        val parent = switchCompat.parent as? ViewGroup
        parent?.removeView(switchCompat) // 기존 SwitchCompat 제거

        val newSwitch = SwitchCompat(requireContext()).apply {
            trackDrawable = ContextCompat.getDrawable(context, R.drawable.toggle_bg)
        }
        parent?.addView(newSwitch) // 새로운 SwitchCompat 추가

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}