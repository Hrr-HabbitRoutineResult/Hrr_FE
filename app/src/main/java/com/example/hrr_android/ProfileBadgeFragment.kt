package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.FragmentProfileBadgeBinding

class ProfileBadgeFragment : Fragment() {
    private lateinit var binding: FragmentProfileBadgeBinding       //뷰 바인딩
    private var typeBadgeList = ArrayList<Badge>()                  //유형 뱃지 리스트
    private var categoryBadgeList = ArrayList<Badge>()              //카테고리 뱃지 리스트

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBadgeBinding.inflate(inflater, container, false)

        //유형 뱃지 더미 데이터 - 테스트 시 주석 해제 or 설정
        typeBadgeList.apply {
            add(Badge("뱃지명", R.drawable.img_badge_challenge_01))
            add(Badge("오늘부터 챌린저", R.drawable.img_badge_challenge_01))
            add(Badge("뱃지 이름이 이렇게 길어지면 어떻게 될까요", R.drawable.img_badge_challenge_01))
            add(Badge("뱃지명", R.drawable.img_badge_challenge_01))
            add(Badge("뱃지명", R.drawable.img_badge_challenge_01))
        }

        //유형 뱃지 RecyclerView 연결
        val typeBadgeRVAdapter = ProfileBadgeRVAdapter(typeBadgeList)
        binding.rvProfileBadgeType.adapter = typeBadgeRVAdapter
        binding.rvProfileBadgeType.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //카테고리 뱃지 더미 데이터 - 테스트 시 주석 해제 or 설정
        categoryBadgeList.apply {
            add(Badge("뱃지명", R.drawable.img_badge_health_03))
            add(Badge("프로 운동러", R.drawable.img_badge_health_03))
            add(Badge("뱃지 이름이 이렇게 길어지면 어떻게 될까요", R.drawable.img_badge_health_03))
            add(Badge("뱃지명", R.drawable.img_badge_health_03))
            add(Badge("뱃지명", R.drawable.img_badge_health_03))
        }

        //카테고리 뱃지 RecyclerView 연결
        val categoryBadgeRVAdapter = ProfileBadgeRVAdapter(categoryBadgeList)
        binding.rvProfileBadgeCategory.adapter = categoryBadgeRVAdapter
        binding.rvProfileBadgeCategory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //데이터 유무 판단하여 뷰 전환
        if(typeBadgeList.size != 0 || categoryBadgeList.size != 0){
            //뱃지 획득 시
            binding.clProfileBadgeContentNo.visibility = View.GONE
            binding.clProfileBadgeMy.visibility = View.VISIBLE

            if(typeBadgeList.size != 0 && categoryBadgeList.size != 0){
                //유형, 카테고리 뱃지 모두 획득 시
                binding.rvProfileBadgeType.visibility = View.VISIBLE
                binding.rvProfileBadgeCategory.visibility = View.VISIBLE
                binding.clProfileBadgeTypeNo.visibility = View.GONE
                binding.clProfileBadgeCategoryNo.visibility = View.GONE
            }

            else if(typeBadgeList.size != 0 && categoryBadgeList.size == 0){
                //유형 뱃지 획득 시
                binding.rvProfileBadgeType.visibility = View.VISIBLE
                binding.clProfileBadgeTypeNo.visibility = View.GONE
            }

            else if(typeBadgeList.size == 0 && categoryBadgeList.size != 0){
                //카테고리 뱃지 획득 시
                binding.rvProfileBadgeCategory.visibility = View.VISIBLE
                binding.clProfileBadgeCategoryNo.visibility = View.GONE
            }
        }

        return binding.root
    }

}