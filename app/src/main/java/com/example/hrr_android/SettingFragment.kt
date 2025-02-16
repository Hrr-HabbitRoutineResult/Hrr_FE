package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.hrr_android.access.AuthViewModel
import com.example.hrr_android.access.ui.LoginActivity
import com.example.hrr_android.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ViewBinding 초기화
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListener()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initClickListener() {
        // 계정 설정
        binding.llSettingAccount.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fl_profile_more_fragment_container, SettingAccountFragment())
                .commit()

            (activity as? ProfileMoreActivity)?.setTitle("계정 설정")

        }


    }

}