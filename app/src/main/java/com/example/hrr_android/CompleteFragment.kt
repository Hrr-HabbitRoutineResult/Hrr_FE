package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.databinding.FragmentCompleteBinding

class CompleteFragment : Fragment() {

    private var _binding: FragmentCompleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 다음 버튼 클릭 시 MainActivity로 이동
        binding.btnCompleteNext.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK  // 뒤로가기를 눌렀을 때 의도적으로 앱 종료
            }
            startActivity(intent)
            activity?.finish()  // SignUpActivity 종료
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}