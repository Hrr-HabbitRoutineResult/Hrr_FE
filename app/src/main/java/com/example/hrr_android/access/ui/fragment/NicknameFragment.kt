package com.example.hrr_android.access.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.MainActivity
import com.example.hrr_android.R
import com.example.hrr_android.access.ui.SignUpActivity
import com.example.hrr_android.databinding.FragmentCompleteBinding
import com.example.hrr_android.databinding.FragmentNicknameBinding

class NicknameFragment : Fragment() {

    private var _binding: FragmentNicknameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNicknameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 다음 버튼 클릭 시 CompleteFragment로 이동
        binding.btnNicknameNext.setOnClickListener {
            (activity as? SignUpActivity)?.changeFragment(CompleteFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}