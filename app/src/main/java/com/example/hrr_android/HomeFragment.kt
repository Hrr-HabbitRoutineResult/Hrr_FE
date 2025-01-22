package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
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

        // 동적 인디케이터 설정
        setupDynamicIndicator(challengeCardList)

        // ViewPager2 연결
        binding.vpHomeChallenge.apply {
            adapter = challengeAdapter

            // 데이터가 없으면 visibility를 GONE으로 설정
            visibility = if (challengeCardList.isEmpty()) View.GONE else View.VISIBLE
        }

        // 데이터가 있을 때 include 한 레이아웃 숨기고 인디케이터 표시
        if (challengeCardList.isEmpty()) {
            binding.itemHomeChallengeCardNew.root.visibility = View.VISIBLE
            binding.indicatorHomeChallengeCard.visibility = View.GONE
        } else {
            binding.itemHomeChallengeCardNew.root.visibility = View.GONE
            binding.indicatorHomeChallengeCard.visibility = View.VISIBLE
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
            Challenge("열 자가 최대라길래", R.drawable.img_cook, "", true, false)
        )

        // 참여한 챌린지가 없을 때
        // return emptyList()
    }

    private fun setupDynamicIndicator(challengeList: List<Challenge>) {
        val indicatorContainer = binding.indicatorHomeChallengeCard
        indicatorContainer.removeAllViews() // 초기화

        // 실제 어댑터에 표시될 총 아이템 수 계산 (None 뷰 포함)
        val totalItems = if (challengeList.size < 5) challengeList.size + 1 else 5

        // 동적으로 인디케이터 추가
        for (i in 0 until totalItems) {
            val dot = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    if (i == 0) 80 else 16, 16 // 첫 번째 아이템은 Active 상태로 시작
                ).apply {
                    marginEnd = 16 // 점 간격
                }
                setBackgroundResource(if (i == 0) R.drawable.indicator_active else R.drawable.indicator_inactive)
            }
            indicatorContainer.addView(dot)
        }

        // ViewPager2 페이지 변경 시 인디케이터 상태 업데이트
        binding.vpHomeChallenge.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                for (i in 0 until indicatorContainer.childCount) {
                    val dot = indicatorContainer.getChildAt(i)
                    val layoutParams = dot.layoutParams as LinearLayout.LayoutParams
                    if (i == position) {
                        dot.setBackgroundResource(R.drawable.indicator_active)
                        layoutParams.width = 80 // Active 크기
                        layoutParams.height = 16
                    } else {
                        dot.setBackgroundResource(R.drawable.indicator_inactive)
                        layoutParams.width = 16 // Inactive 크기
                        layoutParams.height = 16
                    }
                    dot.layoutParams = layoutParams
                }
            }
        })
    }
}