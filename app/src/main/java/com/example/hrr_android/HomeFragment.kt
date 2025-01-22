package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryRVAdapter
    private lateinit var challengeAdapter: ChallengeCardVPAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ViewBinding 초기화
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 더미데이터
        val categoryList = listOf(
            Category(R.drawable.ic_home_all, "전체보기"),
            Category(R.drawable.ic_home_health, "운동"),
            Category(R.drawable.ic_home_learn, "학업"),
            Category(R.drawable.ic_home_company, "취업준비"),
            Category(R.drawable.ic_home_routine, "생활습관"),
            Category(R.drawable.ic_home_hobby, "취미")
        )

        // Adapter 초기화
        categoryAdapter = CategoryRVAdapter(categoryList)

        // 가로 스크롤의 RecyclerView 연결
        binding.rvHomeCategory.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }

        // 홈 화면에서만 사용하는 인기 챌린지 아이콘
        binding.itemHomeChallengeSummary.ivChallengePopular.visibility = View.VISIBLE

        // 챌린지 데이터 리스트
        val challengeCardList = loadChallengeData() // 데이터 로드 함수 호출

        // Adapter 초기화
        challengeAdapter = ChallengeCardVPAdapter(challengeCardList)

        // ViewPager2 연결
        binding.vpHomeChallenge.apply {
            adapter = challengeAdapter

            // 데이터가 없으면 visibility를 GONE으로 설정
            visibility = if (challengeCardList.isEmpty()) View.GONE else View.VISIBLE
        }

        // 데이터가 있을 때 include 한 레이아웃 숨기기
        if (challengeCardList.isEmpty()) {
            binding.itemHomeChallengeCardNew.root.visibility = View.VISIBLE
        } else {
            binding.itemHomeChallengeCardNew.root.visibility = View.GONE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 챌린지 데이터 로드 함수
    private fun loadChallengeData(): List<Challenge> {
        // 참여한 챌린지가 있을 때
        return listOf(
            Challenge("공부합시당", R.drawable.img_study, "", true, true),
            Challenge("달리기 하실 분", R.drawable.img_running, "", false, true),
            Challenge("열 자가 최대라길래", R.drawable.img_cook, "", true, false),
        )

        // 사용자가 챌린지에 참여하지 않았을 때
        // return emptyList()
    }
}