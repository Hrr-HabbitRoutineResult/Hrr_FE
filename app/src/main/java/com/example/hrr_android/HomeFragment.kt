package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.hrr_android.access.ui.LoginActivity
import com.example.hrr_android.databinding.FragmentHomeBinding
import com.example.hrr_android.access.AuthViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryRVAdapter
    private lateinit var challengeAdapter: ChallengeCardVPAdapter
    private lateinit var hotPostAdapter: HotPostRVAdapter

    private lateinit var authViewModel: AuthViewModel

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

        // 뷰 페이저 미리보기 설정
        setupViewPagerPreview()

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

        // 더미데이터
        val hotPostList = listOf(
            HotPost("운동게시판", "어제 PT 갔거든? 근데 피티쌤이"),
            HotPost("학업게시판", "교수님이 아무래도 내가 자기 수업만 듣는 줄 아는 것 같아 그렇지 않고서야"),
            HotPost("취업준비게시판", "IT 계열인데 서류는 통과했어"),
            HotPost("생활습관게시판", "우리 챌린지 방 스터디가 잘 운영이 안되는 것 같아"),
            HotPost("공공기관/공무원/정출연 취준생", "해커스 공기업 NCS 통합 봉모 주황이 1회차")
        )

        // 더미 데이터가 없는 상태
        // val hotPostList = listOf<HotPost>()

        // 데이터 확인 후 visibility 조정
        if (hotPostList.isEmpty()) {
            binding.rvHomeHotPost.visibility = View.GONE
            binding.layoutHomePopularPost.visibility = View.VISIBLE
        } else {
            binding.rvHomeHotPost.visibility = View.VISIBLE
            binding.layoutHomePopularPost.visibility = View.GONE
        }

        // Adapter 초기화
        hotPostAdapter = HotPostRVAdapter(hotPostList)

        binding.rvHomeHotPost.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = hotPostAdapter
        }

        // 알림 화면으로 이동
        binding.ivHomeAlarm.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        // 게시판으로 이동
        binding.tvHomeMore.setOnClickListener {
            val communityFragment = CommunityFragment()

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, communityFragment)
                .commit()

            // 바텀 네비게이션 아이템 변경 (뷰 바인딩 사용)
            val activityBinding = (requireActivity() as MainActivity).getBinding()
            activityBinding.mainBottomNavi.selectedItemId = R.id.navi_community
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

    private fun setupViewPagerPreview() {
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pageWidth)
        val screenWidth = resources.displayMetrics.widthPixels  // 스마트폰 너비 길이
        val offsetPx = screenWidth - pageMarginPx - pagerWidth

        binding.vpHomeChallenge.setPageTransformer { page, position ->
            val scaleFactor = 0.70f + (1 - Math.abs(position)) * 0.30f // 중앙 아이템은 크게, 양옆은 작게
            page.scaleY = scaleFactor
            page.scaleX = scaleFactor
            page.translationX = position * -offsetPx
            page.alpha = 0.7f + (1 - Math.abs(position)) * 0.3f // 양옆 아이템 투명도 조절
        }

        binding.vpHomeChallenge.offscreenPageLimit = 1  // 미리 로드할 페이지 수 설정
    }

    private fun logout() {
        authViewModel.logout() // ViewModel에서 로그아웃 처리
        moveToLoginActivity() // 로그인 화면으로 이동
    }

    private fun moveToLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // 현재 액티비티 종료
    }
}