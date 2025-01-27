package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.databinding.FragmentCommunityBinding

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 기본 게시판 데이터 설정
        val defaultCommunity = mutableListOf(
            Community(true, "운동게시판", ""),
            Community(false, "학업게시판", ""),
            Community(false, "취업준비게시판", ""),
            Community(false, "생활습관게시판", ""),
            Community(false, "취미게시판", "")
        )

        // 기본 게시판 RecyclerView 설정
        setupMainRecyclerView(binding.rvCommunityCategory, defaultCommunity)

        // 소게시판 섹션 데이터 설정
        val sections = listOf(
            // 운동 섹션
            SectionData(
                recyclerView = binding.rvCommunityHealth,
                dropdownLayout = binding.layoutCommunityDropdownHealth,
                dropdownButton = binding.btnCommunityDropdownHealth,
                data = mutableListOf(
                    Community(true, "헬스 게시판", "pt 다니시는 분들을 위한 게시판", BaseCommunity.HEALTH),
                    Community(false, "건강하게 다이어트", "운동하면서 다이어트 합시다", BaseCommunity.HEALTH)
                )
            ),
            // 학업 섹션
            SectionData(
                recyclerView = binding.rvCommunityLearn,
                dropdownLayout = binding.layoutCommunityDropdownLearn,
                dropdownButton = binding.btnCommunityDropdownLearn,
                data = mutableListOf(
                    Community(true, "정시 게시판", "정시를 준비하는 학생들을 위한 게시판", BaseCommunity.LEARN),
                    Community(false, "수시 게시판", "수시를 준비하는 학생들을 위한 게시판", BaseCommunity.LEARN),
                    Community(true, "토익/토플/오픽", "영어 자격증 정보 공유", BaseCommunity.LEARN),
                    Community(true, "일어 게시판", "일본어 같이 공부해요!", BaseCommunity.LEARN)
                )
            ),
            // 취업 섹션
            SectionData(
                recyclerView = binding.rvCommunityCompany,
                dropdownLayout = binding.layoutCommunityDropdownCompany,
                dropdownButton = binding.btnCommunityDropdownCompany,
                data = mutableListOf(
                Community(true, "공공기관/공무원/정출연 취준생", "취준생 분들만 들어와주세요!", BaseCommunity.COMPANY),
                    Community(false, "코딩/CS/개발", "IT 관련 질문, 잡담, 정보 공유", BaseCommunity.COMPANY),
                    Community(true, "IT 기업 입사하기", "IT 기업 정보 공유하고 함께 준비해요!", BaseCommunity.COMPANY)
                )
            ),
            // 생활 루틴 섹션
            SectionData(
                recyclerView = binding.rvCommunityRoutine,
                dropdownLayout = binding.layoutCommunityDropdownRoutine,
                dropdownButton = binding.btnCommunityDropdownRoutine,
                data = mutableListOf()
            ),
            // 취미 섹션
            SectionData(
                recyclerView = binding.rvCommunityHobby,
                dropdownLayout = binding.layoutCommunityDropdownHobby,
                dropdownButton = binding.btnCommunityDropdownHobby,
                data = mutableListOf(
                    Community(true, "뜨개질 게시판", "뜨개질 도안 및 결과물 공유해요", BaseCommunity.HOBBY)
                )
            )
        )

        // 소게시판 RecyclerView 설정
        sections.forEach { setupRecyclerView(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupMainRecyclerView(recyclerView: RecyclerView, data: MutableList<Community>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CommunityRVAdapter(data)
    }

    private fun setupRecyclerView(section: SectionData) {
        with(section) {
            if (data.isEmpty()) {
                // 데이터가 없으면 버튼 비활성화
                dropdownLayout.isEnabled = false
                dropdownButton.visibility = View.GONE
                return
            }

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = CommunityRVAdapter(data)
            recyclerView.visibility = View.GONE // 초기 상태는 GONE

            recyclerView.isNestedScrollingEnabled = false

            // RecyclerView 드롭다운 토글
            dropdownLayout.setOnClickListener {
                toggleRecyclerViewVisibility(recyclerView, dropdownButton)
            }
        }
    }

    private fun toggleRecyclerViewVisibility(
        recyclerView: RecyclerView,
        dropdownButton: ImageView
    ) {
        val isVisible = recyclerView.visibility == View.VISIBLE
        recyclerView.visibility = if (isVisible) View.GONE else View.VISIBLE
        dropdownButton.setImageResource(
            if (isVisible) R.drawable.ic_community_arrow_down else R.drawable.ic_community_arrow_up
        )
    }

    data class SectionData(
        val recyclerView: RecyclerView,
        val dropdownLayout: View,
        val dropdownButton: ImageView,
        val data: MutableList<Community>
    )
}
