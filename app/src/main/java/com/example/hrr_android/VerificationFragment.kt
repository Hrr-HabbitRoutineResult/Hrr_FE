package com.example.hrr_android

import android.icu.text.IDNA.Info
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.databinding.FragmentVerificationBinding

class VerificationFragment : Fragment() {
    private var _binding: FragmentVerificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnVerificationNext.setOnClickListener {
            (activity as? SignUpActivity)?.changeFragment(InfoInputFragment())  // 가입정보 입력 프래그먼트로 이동
        }

    }

    override fun onDestroyView() {
        binding.btnVerificationNext.setOnClickListener(null)
        _binding = null
        super.onDestroyView()
    }
}