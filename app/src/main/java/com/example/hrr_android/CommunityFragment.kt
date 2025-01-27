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

        // 더미데이터
        val community = mutableListOf(
            Community(true, "운동게시판", ""),
            Community(false, "학업게시판", ""),
            Community(false, "취업준비게시판", ""),
            Community(false, "생활습관게시판", ""),
            Community(false, "취미게시판", "")
        )

        // 리사이클러뷰 설정
        val adapter = CommunityRVAdapter(community)
        binding.rvCommunityCategory.adapter = adapter
        binding.rvCommunityCategory.layoutManager = LinearLayoutManager(requireContext())

        // 데이터 초기화 및 RecyclerView 설정
        setupRecyclerView(
            binding.rvCommunityHealth,
            binding.layoutCommunityDropdownHealth,
            binding.btnCommunityDropdownHealth,
            mutableListOf(
                Community(true, "헬스 게시판", "pt 다니시는 분들을 위한 게시판"),
                Community(false, "건강하게 다이어트", "운동하면서 다이어트 합시다")
            )
        )

        setupRecyclerView(
            binding.rvCommunityLearn,
            binding.layoutCommunityDropdownLearn,
            binding.btnCommunityDropdownLearn,
            mutableListOf(
                Community(true, "정시 게시판", "정시를 준비하는 학생들을 위한 게시판"),
                Community(false, "수시 게시판", "수시를 준비하는 학생들을 위한 게시판"),
                Community(true, "토익/토플/오픽", "영어 자격증 정보 공유"),
                Community(true, "일어 게시판", "일본어 같이 공부해요!")
            )
        )

        setupRecyclerView(
            binding.rvCommunityCompany,
            binding.layoutCommunityDropdownCompany,
            binding.btnCommunityDropdownCompany,
            mutableListOf(
                Community(true, "공공기관/공무원/정출연 취준생", "취준생 분들만 들어와주세요! 얘도 너무 길어지면 중간에 잘려서 점으로 표시되었으면 좋겠는데"),
                Community(false, "코딩/CS/개발", "IT 관련 질문, 잡담, 정보 공유"),
                Community(true, "IT 기업 입사하기", "IT 기업 정보 공유하고 함께 준비해요!")
            )
        )

        setupRecyclerView(
            binding.rvCommunityRoutine,
            binding.layoutCommunityDropdownRoutine,
            binding.btnCommunityDropdownRoutine,
            mutableListOf()
        )

        setupRecyclerView(
            binding.rvCommunityHobby,
            binding.layoutCommunityDropdownHobby,
            binding.btnCommunityDropdownHobby,
            mutableListOf(
                Community(true, "뜨개질 게시판", "뜨개질 도안 및 결과물 공유해요")
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        dropdownLayout: View,
        dropdownButton: ImageView,
        data: MutableList<Community>
    ) {
        if (data.isEmpty()) {
            dropdownLayout.setOnClickListener(null)
            dropdownButton.setImageResource(R.drawable.ic_community_arrow_down)
            return
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CommunityRVAdapter(data)
        recyclerView.visibility = View.GONE // 초기 상태는 GONE

        recyclerView.isNestedScrollingEnabled = false

        // 동적 높이 계산
        recyclerView.post {
            val totalHeight = recyclerView.adapter?.itemCount?.let { count ->
                val viewHolder = recyclerView.adapter?.createViewHolder(
                    recyclerView,
                    recyclerView.adapter!!.getItemViewType(0)
                )
                viewHolder?.itemView?.measure(0, 0)
                val itemHeight = viewHolder?.itemView?.measuredHeight ?: 0
                itemHeight * count
            }
            recyclerView.layoutParams.height = totalHeight ?: recyclerView.layoutParams.height
            recyclerView.requestLayout()
        }

        dropdownLayout.setOnClickListener {
            toggleRecyclerViewVisibility(recyclerView, dropdownButton)
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
}
