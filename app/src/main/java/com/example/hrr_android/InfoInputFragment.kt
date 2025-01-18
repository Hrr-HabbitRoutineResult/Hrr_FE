package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.databinding.FragmentInfoInputBinding

class InfoInputFragment : Fragment() {

    private var _binding: FragmentInfoInputBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 뷰 바인딩 초기화
        _binding = FragmentInfoInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnInfoInputNext.setOnClickListener {
            (activity as? SignUpActivity)?.changeFragment(CompleteFragment())  //  가입완료 프래그먼트로 이동
        }
    }

    override fun onDestroyView() {
        binding.btnInfoInputNext.setOnClickListener(null)
        _binding = null
        super.onDestroyView()
    }
}