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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}