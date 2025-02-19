package com.example.hrr_android.challengelist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.R
import com.example.hrr_android.databinding.FragmentSearchChallengeBinding
import com.example.hrr_android.databinding.LayoutChallengeSearchHeaderBinding
import com.example.hrr_android.makechallenge.MakeChallengeFragment

class SearchChallengeFragment : Fragment() {

    private var _binding: FragmentSearchChallengeBinding? = null
    private val binding get() = _binding!!

    private var _headerBinding: LayoutChallengeSearchHeaderBinding? = null
    private val headerBinding get() = _headerBinding!!

    private lateinit var challengeAdapter: ChallengeListRVAdapter

    private val allChallenges = listOf(
        ChallengeItem("운동", true, "운동 습관 기르기", "매일", "6개월", 10, 20, false),
        ChallengeItem("공부", false, "매일 공부 루틴", "주 3회", "3개월", 25, 30, true),
        ChallengeItem("취업준비", true, "면접 준비 스터디", "주 1회", "6개월", 50, 50, false)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchChallengeBinding.inflate(inflater, container, false)
        _headerBinding = LayoutChallengeSearchHeaderBinding.bind(binding.layoutChallengeSearchHeader.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        val searchEditText = headerBinding.etSearchChallenge
        val searchButton = headerBinding.ivSearch
        val eraseButton = headerBinding.ivEraseSearch

        headerBinding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // RecyclerView 초기 설정
        challengeAdapter = ChallengeListRVAdapter()
        binding.rvChallengeListSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChallengeListSearch.adapter = challengeAdapter

        // 검색창에서 엔터 키 입력 시 검색 실행 및 키보드 내리기
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                performSearch()
                hideKeyboard(searchEditText)
                true
            } else {
                false
            }
        }

        // 검색 버튼 클릭 시 검색 실행
        searchButton.setOnClickListener {
            performSearch()
            hideKeyboard(searchEditText)
        }

        // x 버튼 클릭 시 검색어 초기화
        eraseButton.setOnClickListener {
            searchEditText.text.clear()
        }

        makeChallengeButtonClick()
    }

    // 검색 실행 함수
    private fun performSearch() {
        val query = headerBinding.etSearchChallenge.text.toString().trim()
        updateSearchResults(query)
    }

    // 검색 결과 업데이트
    private fun updateSearchResults(query: String) {
        val filteredList = if (query.isEmpty()) {
            emptyList()
        } else {
            allChallenges.filter { it.title.contains(query, ignoreCase = true) }
        }

        challengeAdapter.updateList(filteredList)

        when {
            query.isEmpty() -> { // 검색창이 비었을 때
                binding.clSearchChallenge.visibility = View.VISIBLE
                binding.clSearchChallengeO.visibility = View.GONE
                binding.clSearchChallengeX.visibility = View.GONE
            }
            filteredList.isEmpty() -> { // 검색 결과 없을 때
                binding.clSearchChallenge.visibility = View.GONE
                binding.clSearchChallengeO.visibility = View.GONE
                binding.clSearchChallengeX.visibility = View.VISIBLE
            }
            else -> { // 검색 결과 있을 때
                binding.clSearchChallenge.visibility = View.GONE
                binding.clSearchChallengeO.visibility = View.VISIBLE
                binding.clSearchChallengeX.visibility = View.GONE
            }
        }
    }

    // 키보드 숨기기 함수 (안정적인 방식)
    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun makeChallengeButtonClick() {
        binding.btnCreateChallenge.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, MakeChallengeFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _headerBinding = null
    }
}
