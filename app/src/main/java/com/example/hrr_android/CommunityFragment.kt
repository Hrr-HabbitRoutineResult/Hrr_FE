package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
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

        binding.btnCommunityFloating.setOnClickListener {
            // 추후에 구현 예정
            Toast.makeText(requireContext(), "게시판 개설로 이동", Toast.LENGTH_SHORT).show()
        }

        // 버튼 클릭 이벤트 설정
        setupButtonListeners()

        // 기본 게시판 데이터 설정
        val defaultCommunity = mutableListOf(
            Community(true, "운동게시판", "", null),
            Community(false, "학업게시판", "", null),
            Community(false, "취업준비게시판", "", null),
            Community(false, "생활습관게시판", "", null),
            Community(false, "취미게시판", "", null)
        )

        // 기본 게시판 RecyclerView 설정
        setupMainRecyclerView(binding.rvCommunityCategory, defaultCommunity)

        // 소게시판 섹션 데이터 설정
        val sections = listOf(
            SectionData(
                recyclerView = binding.rvCommunityHealth,
                dropdownLayout = binding.layoutCommunityDropdownHealth,
                dropdownButton = binding.btnCommunityDropdownHealth,
                data = mutableListOf(
                    Community(true, "헬스 게시판", "pt 다니시는 분들을 위한 게시판", BaseCommunity.HEALTH),
                    Community(false, "건강하게 다이어트", "운동하면서 다이어트 합시다", BaseCommunity.HEALTH)
                )
            ),
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
            SectionData(
                recyclerView = binding.rvCommunityRoutine,
                dropdownLayout = binding.layoutCommunityDropdownRoutine,
                dropdownButton = binding.btnCommunityDropdownRoutine,
                data = mutableListOf()
            ),
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

    private fun setupButtonListeners() {
        binding.btnCommunityHot.setOnClickListener {
            navigateToBoardActivity("인기글", null, "CommunityTabContentFragment")
        }
        binding.btnCommunityMyPost.setOnClickListener {
            navigateToBoardActivity("내 게시글", null, "CommunityTabContentFragment")
        }
        binding.btnCommunityComment.setOnClickListener {
            navigateToBoardActivity("댓글 단 글", null, "CommunityTabContentFragment")
        }
        binding.btnCommunitySaved.setOnClickListener {
            navigateToBoardActivity("저장한 글", null, "CommunityTabContentFragment")
        }
    }

    private fun navigateToBoardActivity(baseCategory: String, subTitle: String?, fragment: String) {
        val intent = Intent(requireContext(), BoardActivity::class.java).apply {
            putExtra("baseCategory", baseCategory) // 타이틀
            putExtra("subTitle", subTitle ?: "")  // 서브타이틀 (없으면 공백)
            putExtra("fragment", fragment)        // 표시할 프래그먼트
        }
        startActivity(intent)
    }

    private fun setupMainRecyclerView(recyclerView: RecyclerView, data: MutableList<Community>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CommunityRVAdapter(data) { community ->
            // 타이틀은 community.title, 서브 타이틀은 없음
            navigateToBoardActivity(community.title, null, "CommunityContentFragment")
        }
    }

    private fun setupRecyclerView(section: SectionData) {
        with(section) {
            if (data.isEmpty()) {
                dropdownLayout.isEnabled = false
                dropdownButton.visibility = View.GONE
                return
            }

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = CommunityRVAdapter(data) { community ->
                // 타이틀은 community.baseCommunity, 서브 타이틀은 community.title
                navigateToBoardActivity(
                    baseCategory = community.parentCategory?.title ?: community.title, // BaseCommunity.title 또는 Community.title
                    subTitle = if (community.parentCategory != null) community.title else null,
                    fragment = "CommunityContentFragment"
                )
            }
            recyclerView.visibility = View.GONE

            recyclerView.isNestedScrollingEnabled = false

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
