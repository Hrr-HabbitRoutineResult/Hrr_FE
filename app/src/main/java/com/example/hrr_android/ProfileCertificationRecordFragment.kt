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

//        certificationList.apply {
//            add(Certification("챌린지명", "게시글 제목", "2024.12.31", R.drawable.img_running, true))
//            add(Certification("Run To You", "마지막 인증합니다~^^", "2024.12.31", R.drawable.img_running, true))
//            add(Certification("사진 없을 경우", "게시글 제목", "2025.01.01", hasLink = false))
//            add(Certification("Run To You", "1년 만에 인증합니다~^^", "2025.01.01", R.drawable.img_running, false))
//            add(Certification("인증 제목 없을 경우", "", "2025.01.01", R.drawable.img_running, false))
//        }

        //데이터 유무 판단하여 뷰 전환
        if(certificationList.size != 0){
            binding.clProfileCertificationRecordContentNo.visibility = View.GONE
            binding.rvProfileCertificationRecored.visibility = View.VISIBLE
        }

        val profileCertificationRVAdapter = ProfileCertificationRVAdapter(certificationList)
        binding.rvProfileCertificationRecored.adapter = profileCertificationRVAdapter
        binding.rvProfileCertificationRecored.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }

}