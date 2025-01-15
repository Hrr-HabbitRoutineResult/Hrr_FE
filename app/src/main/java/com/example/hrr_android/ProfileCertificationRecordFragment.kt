package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.databinding.FragmentProfileCertificationRecordBinding

class ProfileCertificationRecordFragment : Fragment() {
    private lateinit var binding: FragmentProfileCertificationRecordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileCertificationRecordBinding.inflate(inflater, container, false)

        return binding.root
    }

}