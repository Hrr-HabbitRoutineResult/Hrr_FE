package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hrr_android.databinding.FragmentProfileBadgeMoreBinding

class ProfileBadgeMoreFragment : Fragment(), OnBadgeClickListener {
    //뷰 바인딩
    private var _binding: FragmentProfileBadgeMoreBinding? = null
    private val binding get() = _binding!!
    //뱃지 리스트
    private var typeBadgeList = ArrayList<Badge>()
    private var categoryBadgeList = ArrayList<Badge>()

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
        typeBadgeList.apply {
            add(Badge("오늘부터 챌린저", R.drawable.badge_type_fromtoday_challenger, true))
            add(Badge("아마추어 챌린지", R.drawable.badge_type_amateur_challenger, true))
            add(Badge("챌린저 매니아", R.drawable.badge_type_challenge_mania, true))
            add(Badge("프로 챌린저", R.drawable.badge_type_pro_challenger, true))
            add(Badge("챌린지 마스터", R.drawable.badge_type_challenger_master))
            add(Badge("챌생챌사", R.drawable.badge_type_challenge_live_die))
            add(Badge("다채로운 체험가", R.drawable.badge_type_colorful_experience))
            add(Badge("올라운더", R.drawable.badge_type_allrounder))
            add(Badge("이정도면 갓생러", R.drawable.badge_type_godlife))
            add(Badge("내 하루는 48시간", R.drawable.badge_type_myday_48h))
            add(Badge("Let's 스터디", R.drawable.badge_type_lets_study, true))
            add(Badge("수준급 스터디언", R.drawable.badge_type_good_studian, true))
            add(Badge("스터디 고수", R.drawable.badge_type_study_king))
        }

        //유형 뱃지 RecyclerView 연결
        val typeBadgeMoreRVAdapter = ProfileBadgeMoreRVAdapter(typeBadgeList, this)
        binding.rvBadgeMoreType.apply {
            adapter = typeBadgeMoreRVAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

        //카테고리 뱃지 더미 데이터
        categoryBadgeList.apply {
            add(Badge("운동 스타터", R.drawable.badge_category_exercise_starter, true))
            add(Badge("운동 ing", R.drawable.badge_category_exercise_ing, true))
            add(Badge("프로 운동러", R.drawable.badge_category_pro_exercise, true))
            add(Badge("운동 마스터", R.drawable.badge_category_exercise_master, true))
            add(Badge("학업 스타터", R.drawable.badge_category_study_starter, true))
            add(Badge("지식의 확장", R.drawable.badge_category_extend_knowledge, true))
            add(Badge("공부의 신", R.drawable.badge_category_god_of_study, true))
            add(Badge("학업 마스터", R.drawable.badge_category_study_master))
            add(Badge("취준 스타터", R.drawable.badge_category_job_starter))
            add(Badge("합격의 기운", R.drawable.badge_category_pass_energy))
            add(Badge("합격 프리패스", R.drawable.badge_category_freepass_job))
            add(Badge("취뽀 마스터", R.drawable.badge_category_job_master))
            add(Badge("생활습관 스타터", R.drawable.badge_category_habit_starter))
            add(Badge("작심 N일 돌파", R.drawable.badge_category_beyond_3days))
            add(Badge("칸트 후계자", R.drawable.badge_category_son_of_kant))
            add(Badge("생활습관 마스터", R.drawable.badge_category_habit_master))
            add(Badge("취미 스타터", R.drawable.badge_category_hobby_starter))
            add(Badge("취미 하나쯤은", R.drawable.badge_category_one_hobby))
            add(Badge("나는야 십잡스", R.drawable.badge_category_im_10jobs))
            add(Badge("취미 마스터", R.drawable.badge_category_hobby_master))
        }

        //카테고리 뱃지 RecyclerView 연결
        val categoryBadgeMoreRVAdapter = ProfileBadgeMoreRVAdapter(categoryBadgeList, this)
        binding.rvBadgeMoreCategory.apply {
            adapter = categoryBadgeMoreRVAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBadgeClick(badge: Badge) {
        // 새로운 Activity로 데이터 전달
        val intent = Intent(requireContext(), MyBadgeDetailActivity::class.java).apply {
            putExtra("name", badge.name)
            putExtra("icon", badge.icon)
            putExtra("isObtained", badge.isObtained)
        }
        startActivity(intent)
    }

}