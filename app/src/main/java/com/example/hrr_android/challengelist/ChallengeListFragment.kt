package com.example.hrr_android.challengelist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.R
import com.example.hrr_android.databinding.FragmentChallengeListBinding
import com.example.hrr_android.databinding.LayoutChallengeListFilter1Binding
import com.example.hrr_android.databinding.LayoutChallengeListFilter2Binding
import com.example.hrr_android.databinding.LayoutChallengeListHeaderBinding
import com.example.hrr_android.makechallenge.MakeChallengeFragment

class ChallengeListFragment : Fragment() {

    private var _binding: FragmentChallengeListBinding? = null
    private val binding get() = _binding!!

    private var _headerBinding: LayoutChallengeListHeaderBinding? = null
    private val headerBinding get() = _headerBinding!!

    private var _filter1Binding: LayoutChallengeListFilter1Binding? = null
    private val filter1Binding get() = _filter1Binding!!

    private lateinit var challengeAdapter: ChallengeListRVAdapter
    private lateinit var spinnerFilter: AppCompatSpinner
    private lateinit var cbAvailableChallenge: CheckBox

    private var allChallenges = listOf<ChallengeItem>() // 모든 챌린지 데이터 저장

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChallengeListBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val headerView = binding.root.findViewById<View>(R.id.layout_challenge_list_header)
        _headerBinding = LayoutChallengeListHeaderBinding.bind(headerView)

        val filter1View = binding.root.findViewById<View>(R.id.layout_challenge_list_filter1)
        _filter1Binding = LayoutChallengeListFilter1Binding.bind(filter1View)

        val filter2View = rootView.findViewById<View>(R.id.layout_challenge_list_filter2)
        spinnerFilter = filter2View.findViewById(R.id.spinner_filter)
        cbAvailableChallenge = filter2View.findViewById(R.id.cb_available_challenge)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allChallenges = getChallengeList()
        challengeAdapter = ChallengeListRVAdapter(allChallenges)
        binding.rvChallengeList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChallengeList.adapter = challengeAdapter

        makeChallengeButtonClick()
        setupCheckboxListener()
        setupFilterClickListener()

        filter1Binding.btnFilterAll.isSelected = true

        // 필터 적용 결과 수신
        parentFragmentManager.setFragmentResultListener("filterResult", viewLifecycleOwner) { _, bundle ->
            val category = bundle.getString("category", "전체")
            val type = bundle.getString("type", "전체")
            val duration = bundle.getString("duration", "전체")
            val frequency = bundle.getString("frequency", "전체")
            val week = bundle.getString("week", "전체")
            val people = bundle.getString("people", "전체")

            applyFilter(category, type, duration, frequency, week, people)
        }

        filter1Binding.root.setOnClickListener {
            val filterDialog = FilterDialog()
            filterDialog.show(parentFragmentManager, "FilterDialog")
        }

        setupBackButton()
        setupSearchIconClick()
    }

    private fun setupCheckboxListener() {
        cbAvailableChallenge.setOnCheckedChangeListener { buttonView, isChecked ->
            filterChallenges()

            // ✅ 체크 상태에 따라 텍스트 색상 변경
            val color = if (isChecked) {
                ContextCompat.getColor(requireContext(), R.color.text_primary)
            } else {
                ContextCompat.getColor(requireContext(), R.color.text_tertiary)
            }
            buttonView.setTextColor(color)
        }
    }

    private fun filterChallenges() {
        val filteredList = if (cbAvailableChallenge.isChecked) {
            allChallenges.filter { it.currentPeople < it.maxPeople }
        } else {
            allChallenges
        }

        challengeAdapter.updateList(filteredList)
    }

    private fun setupFilterClickListener() {
        filter1Binding.btnFilterAll.setOnClickListener { showFilterDialog() }
        filter1Binding.btnFilterCategory.setOnClickListener { showFilterDialog() }
        filter1Binding.btnFilterType.setOnClickListener { showFilterDialog() }
        filter1Binding.btnFilterDuration.setOnClickListener { showFilterDialog() }
        filter1Binding.btnFilterFrequency.setOnClickListener { showFilterDialog() }
        filter1Binding.btnFilterDay.setOnClickListener { showFilterDialog() }
        filter1Binding.btnFilterMaxPeople.setOnClickListener { showFilterDialog() }
    }

    private fun showFilterDialog() {
        val filterDialog = FilterDialog()
        filterDialog.show(parentFragmentManager, "FilterDialog")
    }

    private fun applyFilter(
        category: String, type: String, duration: String, frequency: String, week: String, people: String
    ) {
        updateFilterButton(filter1Binding.btnFilterCategory, category, "카테고리")
        updateFilterButton(filter1Binding.btnFilterType, type, "유형")
        updateFilterButton(filter1Binding.btnFilterDuration, duration, "기간")
        updateFilterButton(filter1Binding.btnFilterFrequency, frequency, "빈도")
        updateFilterButton(filter1Binding.btnFilterDay, week, "요일")
        updateFilterButton(filter1Binding.btnFilterMaxPeople, people, "인원")

        val isAllDefault = (category == "전체" && type == "전체" && duration == "전체" &&
                frequency == "전체" && week == "전체" && people == "전체")

        filter1Binding.btnFilterAll.isSelected = isAllDefault

        filterChallenges()
    }

    private fun updateFilterButton(button: AppCompatButton, value: String, defaultText: String) {
        if (value == "전체") {
            button.text = defaultText
            button.isSelected = false
        } else {
            button.text = value
            button.isSelected = true
        }
        adjustFilterButtonWidth(button)
    }

    private fun adjustFilterButtonWidth(button: AppCompatButton) {
        val textLength = button.text.length

        val newWidth = when (textLength) {
            2 -> 54
            3 -> 65.5
            else -> 77
        }

        button.updateLayoutParams<ViewGroup.LayoutParams> {
            width = dpToPx(newWidth.toDouble())
        }
    }

    private fun dpToPx(dp: Double): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun getChallengeList(): List<ChallengeItem> {
        return listOf(
            ChallengeItem("운동", true, "운동 습관 기르기", "매일", "6개월", 10, 20, false),
            ChallengeItem("공부", false, "매일 공부 루틴", "주 3회", "3개월", 30, 30, true),
            ChallengeItem("취업준비", true, "면접 준비 스터디", "주 1회", "6개월", 50, 50, false),
            ChallengeItem("운동", true, "운동 습관 기르기", "매일", "6개월", 10, 20, false),
            ChallengeItem("공부", false, "매일 공부 루틴", "주 3회", "3개월", 25, 30, true),
            ChallengeItem("취업준비", true, "면접 준비 스터디", "주 1회", "6개월", 40, 50, false)
        )
    }

    private fun setupBackButton() {
        headerBinding.btnChallengeListBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun makeChallengeButtonClick() {
        binding.ivCreateChallenge.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, MakeChallengeFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupSearchIconClick() {
        headerBinding.ivSearchIcon.setOnClickListener {
            val searchChallengeFragment = SearchChallengeFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, searchChallengeFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _headerBinding = null
        _filter1Binding = null
    }
}
