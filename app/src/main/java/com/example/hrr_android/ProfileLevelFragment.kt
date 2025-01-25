package com.example.hrr_android

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.core.view.setPadding
import com.example.hrr_android.databinding.FragmentProfileLevelBinding

class ProfileLevelFragment : Fragment() {
    private var _binding: FragmentProfileLevelBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //레벨 달성에 따른 뷰 전환 테스트
        binding.ivLevelBShape.apply {
            setImageResource(R.drawable.bg_level_map_achieved)      //배경 아이콘 변경
        }
        val color = resources.getColor(R.color.white, context?.theme)
        binding.tvLevelB.setTextColor(color)        //색상 변경

        binding.ivLevelSShape.apply {
            setImageResource(R.drawable.bg_level_map_achieved_first)      //배경 아이콘 변경
        }
        val color2 = resources.getColor(R.color.sub_03, context?.theme)
        binding.tvLevelS.setTextColor(color2)        //색상 변경

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}