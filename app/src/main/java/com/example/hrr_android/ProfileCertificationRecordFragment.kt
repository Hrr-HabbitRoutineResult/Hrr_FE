package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.FragmentProfileCertificationRecordBinding

class ProfileCertificationRecordFragment : Fragment() {
    private lateinit var binding: FragmentProfileCertificationRecordBinding
    private var certificationList = ArrayList<Certification>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileCertificationRecordBinding.inflate(inflater, container, false)

        certificationList.apply {
            add(Certification("Run To You", "마지막 인증합니다~^^", "2024.12.31", R.drawable.img_running))
            add(Certification("Run To You", "1년 만에 인증합니다~^^", "2025.01.01", R.drawable.img_running))
        }

        val profileCertificationRVAdapter = ProfileCertificationRVAdapter(certificationList)
        binding.rvProfileCertificationRecored.adapter = profileCertificationRVAdapter
        binding.rvProfileCertificationRecored.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }

}