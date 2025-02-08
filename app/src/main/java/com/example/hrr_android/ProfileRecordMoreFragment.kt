package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.FragmentProfileChallengeMoreBinding
import com.example.hrr_android.databinding.FragmentProfileRecordMoreBinding

class ProfileRecordMoreFragment : Fragment() {
    //뷰 바인딩
    private var _binding: FragmentProfileRecordMoreBinding? = null
    private val binding get() = _binding!!

    //기록 리스트
    private var certifications = ArrayList<Certification>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileRecordMoreBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //챌린지 더미 데이터
        certifications.apply {
            add(Certification("챌린지명", "게시글 제목", "2024.12.31", R.drawable.img_running, true))
            add(Certification("Run To You", "마지막 인증합니다~^^", "2024.12.31", R.drawable.img_running, true))
            add(Certification("사진 없을 경우", "게시글 제목", "2025.01.01", hasLink = false))
            add(Certification("링크 없을 경우", "1년 만에 인증합니다~^^", "2025.01.01", R.drawable.img_running, false))
            add(Certification("인증 제목 없을 경우", "", "2025.01.01", R.drawable.img_running, false))
            add(Certification("테스트", "게시글 제목", "2024.12.31",R.drawable.img_running, true))
            add(Certification("테스트", "게시글 제목", "2024.12.31",R.drawable.img_running, true))
            add(Certification("테스트", "게시글 제목", "2024.12.31",R.drawable.img_running, true))
            add(Certification("테스트", "게시글 제목", "2024.12.31",R.drawable.img_running, true))
            add(Certification("테스트", "게시글 제목", "2024.12.31",R.drawable.img_running, true))
            add(Certification("테스트", "게시글 제목", "2024.12.31",R.drawable.img_running, true))
            add(Certification("테스트", "게시글 제목", "2024.12.31",R.drawable.img_running, true))
        }


        val profileRecordMoreRVAdapter = ProfileRecordMoreRVAdapter(certifications)
        binding.rvRecordMore.apply {
            adapter = profileRecordMoreRVAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}