package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.FragmentProfileCertificationRecordBinding

class ProfileCertificationRecordFragment : Fragment() {
    private var _binding: FragmentProfileCertificationRecordBinding? = null     //뷰 바인딩
    private val binding get() = _binding!!
    private var certificationList = ArrayList<Certification>()                  //인증 기록

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileCertificationRecordBinding.inflate(inflater, container, false)

        // 인증 기록 더미 데이터 - 테스트 시 주석 해제 or 설정
        certificationList.apply {
            add(Certification("챌린지명", "게시글 제목", "2024.12.31", R.drawable.img_running, true))
            add(Certification("Run To You", "마지막 인증합니다~^^", "2024.12.31", R.drawable.img_running, true))
            add(Certification("사진 없을 경우", "게시글 제목", "2025.01.01", hasLink = false))
            add(Certification("링크 없을 경우", "1년 만에 인증합니다~^^", "2025.01.01", R.drawable.img_running, false))
            add(Certification("인증 제목 없을 경우", "", "2025.01.01", R.drawable.img_running, false))
        }

        //데이터 유무 판단하여 뷰 전환
        if(certificationList.size != 0){
            //인증 기록 존재
            binding.clProfileCertificationRecordContentNo.visibility = View.GONE
            binding.rvProfileCertificationRecored.visibility = View.VISIBLE
        }

        //인증 기록 RecyclerView 연결
        val profileCertificationRVAdapter = ProfileCertificationRVAdapter(certificationList)
        binding.rvProfileCertificationRecored.adapter = profileCertificationRVAdapter
        binding.rvProfileCertificationRecored.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //더보기 버튼 클릭 처리
        binding.llProfileCertificationRecordTitle.setOnClickListener{
            val intent = Intent(requireContext(), ProfileMoreActivity::class.java)
            intent.putExtra("type", "certification")
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}