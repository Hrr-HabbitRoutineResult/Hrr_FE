package com.example.hrr_android

import ChallengeCardVPAdapter
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.hrr_android.access.ui.LoginActivity
import com.example.hrr_android.databinding.FragmentHomeBinding
import com.example.hrr_android.access.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : Fragment(), OnChallengeClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryRVAdapter
    private lateinit var challengeAdapter: ChallengeCardVPAdapter
    private lateinit var challengeTodayAdapter: ChallengeTodayVPAdapter
    private lateinit var hotPostAdapter: HotPostRVAdapter

    private lateinit var authViewModel: AuthViewModel
    private val userViewModel: UserViewModel by viewModels()
    private val challengeViewModel: ChallengeViewModel by viewModels()

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

        userViewModel.fetchChallengesOngoing() // API 호출

        setupOngoingViewPager()
        observeChallenges()
        setupViewPagerPreview()

        challengeViewModel.fetchChallengesHotness() // API 호출

        observeChallenges()
        observeChallengesHotness()

        // 더미데이터
/*        val hotPostList = listOf(
            HotPost("운동게시판", "어제 PT 갔거든? 근데 피티쌤이"),
            HotPost("학업게시판", "교수님이 아무래도 내가 자기 수업만 듣는 줄 아는 것 같아 그렇지 않고서야"),
            HotPost("취업준비게시판", "IT 계열인데 서류는 통과했어"),
            HotPost("생활습관게시판", "우리 챌린지 방 스터디가 잘 운영이 안되는 것 같아"),
            HotPost("공공기관/공무원/정출연 취준생", "해커스 공기업 NCS 통합 봉모 주황이 1회차")
        )*/

        // 더미 데이터가 없는 상태
        val hotPostList = listOf<HotPost>()

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupOngoingViewPager() {
        challengeAdapter = ChallengeCardVPAdapter(emptyList(), this)  // 초기 빈 리스트 설정
        binding.vpHomeChallenge.adapter = challengeAdapter
    }

    private fun observeChallenges() {
        userViewModel.challengesOngoing.observe(viewLifecycleOwner) { result ->
            result.onSuccess { challengeList ->
                challengeAdapter = ChallengeCardVPAdapter(challengeList, this)
                binding.vpHomeChallenge.adapter = challengeAdapter

                // 챌린지 개수에 따라 tv_home_subtitle 텍스트 변경
                binding.tvHomeSubTitle.text = if (challengeList.isEmpty()) {
                    "안녕하세요, 흐르르 따라 챌린지에 참가해보세요."
                } else {
                    "안녕하세요, 흐르르 따라 챌린지 인증을 진행해주세요."
                }

                // 챌린지 데이터가 없을 때 UI 처리
                binding.vpHomeChallenge.visibility = if (challengeList.isEmpty()) View.GONE else View.VISIBLE
                binding.itemHomeChallengeCardNew.root.visibility = if (challengeList.isEmpty()) View.VISIBLE else View.GONE
                binding.indicatorHomeChallengeCard.visibility = if (challengeList.isEmpty()) View.GONE else View.VISIBLE

                // 챌린지 목록 프래그먼트로 이동
                binding.itemHomeChallengeCardNew.root.setOnClickListener {
                    // TODO: 챌린지 목록 프래그먼트로 이동하는 클릭 이벤트 추가
                }

                // 인디케이터 설정
                setupChallengeIndicator(challengeList)
            }.onFailure {
                Log.e("HomeFragment", "API 데이터 로드 실패: ${it.message}") // 실패 시 로그 출력
            }
        }
    }


    private fun observeChallengesHotness() {
        challengeViewModel.challengesHotness.observe(viewLifecycleOwner) { result ->

            result?.let { challengeList ->
                challengeTodayAdapter = ChallengeTodayVPAdapter(challengeList, this)
                binding.vpHomeChallengeToday.adapter = challengeTodayAdapter

                // 인디케이터 설정
                setupChallengeTodayIndicator(challengeList)

                // 뷰 페이저 미리보기 설정
                setupTodayViewPagerPreview(
                    binding.vpHomeChallengeToday,
                    challengeTodayAdapter,
                    challengeList,
                    8,
                    32
                )
            } ?: run {
                Log.e("HomeFragment", "API 데이터 로드 실패: 챌린지 데이터가 없습니다.")
            }
        }
    }


    // 커스텀 인디케이터 적용 함수
    private fun setupIndicator(indicatorContainer: LinearLayout, itemCount: Int, viewPager: ViewPager2, activeSize: Int, inactiveSize: Int, margin: Int) {
        indicatorContainer.removeAllViews() // 기존 인디케이터 초기화

        if (itemCount == 0) return // 데이터가 없으면 종료

        for (i in 0 until itemCount) {
            val dot = View(indicatorContainer.context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    if (i == 0) activeSize else inactiveSize, inactiveSize // 첫 번째 아이템은 Active 상태로 시작
                ).apply {
                    marginEnd = margin // 점 간격
                }
                setBackgroundResource(if (i == 0) R.drawable.indicator_active else R.drawable.indicator_inactive)
            }
            indicatorContainer.addView(dot)
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val realPosition = position % itemCount // 무한 스크롤 대비
                for (i in 0 until indicatorContainer.childCount) {
                    val dot = indicatorContainer.getChildAt(i)
                    val layoutParams = dot.layoutParams as LinearLayout.LayoutParams
                    if (i == realPosition) {
                        dot.setBackgroundResource(R.drawable.indicator_active)
                        layoutParams.width = activeSize // Active 크기
                        layoutParams.height = inactiveSize
                    } else {
                        dot.setBackgroundResource(R.drawable.indicator_inactive)
                        layoutParams.width = inactiveSize // Inactive 크기
                        layoutParams.height = inactiveSize
                    }
                    dot.layoutParams = layoutParams
                }
            }
        })
    }

    // 오늘의 인기 챌린지 인디케이터
    private fun setupChallengeTodayIndicator(challengeList: Result<List<ChallengeHotness>>) {
        challengeList.onSuccess { list ->
            setupIndicator(binding.indicatorHomeChallengeToday, list.size, binding.vpHomeChallengeToday, 80, 14, 14)
        }.onFailure {
            setupIndicator(binding.indicatorHomeChallengeToday, 0, binding.vpHomeChallengeToday, 80, 14, 14)
        }
    }

    // 참여 중인 챌린지 인디케이터
    private fun setupChallengeIndicator(challengeList: List<ChallengesOngoing>) {
        val totalItems = if (challengeList.size < 5) challengeList.size + 1 else 5
        setupIndicator(binding.indicatorHomeChallengeCard, totalItems, binding.vpHomeChallenge, 80, 16, 16)
    }

    // 참여 중인 챌린지 뷰 페이저 미리보기
    private fun setupViewPagerPreview() {
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pageWidth)
        val screenWidth = resources.displayMetrics.widthPixels  // 스마트폰 너비 길이
        val offsetPx = screenWidth - pageMarginPx - pagerWidth

        binding.vpHomeChallenge.setPageTransformer { page, position ->
            val scaleFactor = 0.70f + (1 - abs(position)) * 0.30f // 중앙 아이템은 크게, 양옆은 작게
            page.scaleY = scaleFactor
            page.scaleX = scaleFactor
            page.translationX = position * -offsetPx
            page.alpha = 0.7f + (1 - abs(position)) * 0.3f // 양옆 아이템 투명도 조절
        }

        binding.vpHomeChallenge.offscreenPageLimit = 1  // 미리 로드할 페이지 수 설정
    }

    private fun setupTodayViewPagerPreview(
        viewPager: ViewPager2,
        adapter: RecyclerView.Adapter<*>,
        itemList: Result<List<ChallengeHotness>>,
        margin: Int,
        padding: Int
    ) {
        itemList.onSuccess { list ->
            viewPager.apply {
                this.adapter = adapter
                visibility = if (list.isEmpty()) View.GONE else View.VISIBLE

                val listSize = list.size
                if (listSize > 1) {
                    setCurrentItem(listSize, false)

                    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            post {
                                if (position == 0) {
                                    setCurrentItem(listSize, false)
                                } else if (position == listSize * 2 - 1) {
                                    setCurrentItem(listSize - 1, false)
                                }
                            }
                        }
                    })
                }

                clipToPadding = false
                clipChildren = false
                setPadding(padding, 0, padding, 0) // ViewPager 자체 패딩 추가

                // 기존 아이템 데코레이션을 제거하고 다시 추가
                while (itemDecorationCount > 0) {
                    removeItemDecorationAt(0)
                }
                addItemDecoration(HorizontalMarginItemDecoration(margin))

                setPageTransformer { page, position ->
                    page.translationX = position * -2f // 너무 많이 이동하지 않도록 조정
                }

                offscreenPageLimit = 1
            }
        }.onFailure {
            viewPager.visibility = View.GONE // 데이터가 없을 경우 뷰페이저 숨기기
        }
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

    override fun onItemClick(challengeId: Int) {
        val navController = findNavController() // 현재 프래그먼트의 네비게이션 컨트롤러 가져오기

        val bundle = Bundle().apply {
            putInt("challenge_id", challengeId) // 챌린지 ID 전달
        }
        navController.navigate(R.id.action_homeFragment_to_challengeFragment, bundle) // 이동할 프래그먼트 ID 설정
    }

    override fun onMoreClick() {
        Toast.makeText(requireContext(), "더보기를 클릭했습니다", Toast.LENGTH_SHORT).show() // 클릭 확인용
        // TODO: 챌린지 목록 프래그먼트로 이동하는 클릭 이벤트 추가
    }
}