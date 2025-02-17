package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.databinding.FragmentProfileBadgeMoreBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileBadgeMoreFragment : Fragment(), OnBadgeMoreClickListener {
    //뷰 바인딩
    private var _binding: FragmentProfileBadgeMoreBinding? = null
    private val binding get() = _binding!!
    //뱃지 리스트
    private var typeBadgeList = ArrayList<Badge>()
    private var categoryBadgeList = ArrayList<Badge>()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBadgeMoreBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //유형 뱃지 더미 데이터
//        typeBadgeList.apply {
//            add(Badge("오늘부터 챌린저", R.drawable.badge_type_fromtoday_challenger, true, type = "type", listOf(Condition("챌린지 참가 (스터디, 베이직 구분x)", true))))
//            add(Badge("아마추어 챌린지", R.drawable.badge_type_amateur_challenger, true, type = "type"))
//            add(Badge("챌린저 매니아", R.drawable.badge_type_challenge_mania, true, type = "type"))
//            add(Badge("프로 챌린저", R.drawable.badge_type_pro_challenger, true, type = "type"))
//            add(Badge("챌린지 마스터", R.drawable.badge_type_challenger_master, type = "type", obtainCondition = listOf(
//                Condition("챌린지 참가 100회 (스터디, 베이직 구분x)", false))))
//            add(Badge("챌생챌사", R.drawable.badge_type_challenge_live_die, type = "type"))
//            add(Badge("다채로운 체험가", R.drawable.badge_type_colorful_experience, type = "type"))
//            add(Badge("올라운더", R.drawable.badge_type_allrounder, type = "type"))
//            add(Badge("이정도면 갓생러", R.drawable.badge_type_godlife, type = "type"))
//            add(Badge("내 하루는 48시간", R.drawable.badge_type_myday_48h, type = "type"))
//            add(Badge("Let's 스터디", R.drawable.badge_type_lets_study, true, type = "type"))
//            add(Badge("수준급 스터디언", R.drawable.badge_type_good_studian, true, type = "type"))
//            add(Badge("스터디 고수", R.drawable.badge_type_study_king, type = "type"))
//        }

        //유형 뱃지 RecyclerView 연결
        val typeBadgeMoreRVAdapter = ProfileBadgeMoreRVAdapter(typeBadgeList, this)
        binding.rvBadgeMoreType.apply {
            adapter = typeBadgeMoreRVAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

//        //카테고리 뱃지 더미 데이터
//        categoryBadgeList.apply {
//            add(Badge("운동 스타터", R.drawable.badge_category_exercise_starter, true, type = "category", ))
//            add(Badge("운동 ing", R.drawable.badge_category_exercise_ing, true, type = "category", listOf(Condition("운동 챌린지 참가 3회", true), Condition("운동 챌린지 1회 완주 성공" ,false))))
//            add(Badge("프로 운동러", R.drawable.badge_category_pro_exercise, true, type = "category"))
//            add(Badge("운동 마스터", R.drawable.badge_category_exercise_master, true, type = "category"))
//            add(Badge("학업 스타터", R.drawable.badge_category_study_starter, true, type = "category"))
//            add(Badge("지식의 확장", R.drawable.badge_category_extend_knowledge, true, type = "category"))
//            add(Badge("공부의 신", R.drawable.badge_category_god_of_study, true, type = "category"))
//            add(Badge("학업 마스터", R.drawable.badge_category_study_master, type = "category", obtainCondition = listOf(Condition("학업 챌린지 참가 10회", true), Condition("학업 챌린지 5회 완주 성공", false), Condition("학업 스터디 챌린지 3회 완주 성공", true))))
//            add(Badge("취준 스타터", R.drawable.badge_category_job_starter, type = "category"))
//            add(Badge("합격의 기운", R.drawable.badge_category_pass_energy, type = "category"))
//            add(Badge("합격 프리패스", R.drawable.badge_category_freepass_job, type = "category"))
//            add(Badge("취뽀 마스터", R.drawable.badge_category_job_master, type = "category"))
//            add(Badge("생활습관 스타터", R.drawable.badge_category_habit_starter, type = "category"))
//            add(Badge("작심 N일 돌파", R.drawable.badge_category_beyond_3days, type = "category"))
//            add(Badge("칸트 후계자", R.drawable.badge_category_son_of_kant, type = "category"))
//            add(Badge("생활습관 마스터", R.drawable.badge_category_habit_master, type = "category"))
//            add(Badge("취미 스타터", R.drawable.badge_category_hobby_starter, type = "category"))
//            add(Badge("취미 하나쯤은", R.drawable.badge_category_one_hobby, type = "category"))
//            add(Badge("나는야 십잡스", R.drawable.badge_category_im_10jobs, type = "category"))
//            add(Badge("취미 마스터", R.drawable.badge_category_hobby_master, type = "category"))
//        }

        //카테고리 뱃지 RecyclerView 연결
        val categoryBadgeMoreRVAdapter = ProfileBadgeMoreRVAdapter(categoryBadgeList, this)
        binding.rvBadgeMoreCategory.apply {
            adapter = categoryBadgeMoreRVAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

        /*
        * 획득한 뱃지 로딩
        * */
        userViewModel.badges.observe(viewLifecycleOwner) { response->
            typeBadgeList = response?.typeBadges
                ?.map { badge ->
                    Badge(
                        name = badge.name,
                        icon = ValidUtils.getDrawableResId(requireContext(), badge.icon),
                        isObtained = badge.isObtained
                    )
                }
                ?.let { ArrayList(it) } ?: arrayListOf()

            categoryBadgeList = response?.categoryBadges
                ?.map { badge ->
                    Badge(
                        name = badge.name,
                        icon = ValidUtils.getDrawableResId(requireContext(), badge.icon),
                        isObtained = badge.isObtained
                    )
                }
                ?.let { ArrayList(it) } ?: arrayListOf()

            //데이터 유무 판단하여 뷰 전환
            binding.rvBadgeMoreType.adapter?.notifyDataSetChanged()
            binding.rvBadgeMoreCategory.adapter?.notifyDataSetChanged()

        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                val errorToUser = when {
                    it.contains("IllegalStateException") -> "데이터를 불러오는 중 문제가 발생했습니다. 다시 시도해 주세요."
                    it.contains("JsonSyntaxException") -> "서버 응답이 올바르지 않습니다. 업데이트를 확인해 주세요."
                    it.contains("SocketTimeoutException") -> "서버 응답이 지연되고 있습니다. 잠시 후 다시 시도해 주세요."
                    it.contains("IOException") -> "네트워크 연결을 확인해 주세요."
                    else -> "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
                }

                Toast.makeText(requireContext(), errorToUser, Toast.LENGTH_LONG).show()
                Log.e("ProfileFragmentVM", "오류 발생: $errorMsg")
            }
        }
        userViewModel.loadBadges()  // 유저 데이터 로드

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBadgeClick(badge: Badge) {
        // 새로운 Activity로 데이터 전달
        val gson = Gson()
        val badgeJson = gson.toJson(badge)

        val intent = Intent(requireContext(), MyBadgeDetailActivity::class.java).apply {
            putExtra("badgeJson", badgeJson)
        }
        startActivity(intent)

    }

}