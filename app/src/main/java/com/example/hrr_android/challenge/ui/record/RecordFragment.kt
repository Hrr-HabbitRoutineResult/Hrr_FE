package com.example.hrr_android.challenge.ui.record

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.hrr_android.R
import com.example.hrr_android.challenge.model.ChallengeType
import com.example.hrr_android.challenge.ui.record.adapter.RecordPhotoAdapter
import com.example.hrr_android.challenge.ui.record.adapter.RecordTextAdapter
import com.example.hrr_android.databinding.FragmentRecordBinding
import com.example.hrr_android.challenge.ui.common.decoration.GridSpaceItemDecoration
import com.example.hrr_android.challenge.ui.common.decoration.DividerItemDecoration


class RecordFragment : Fragment() {
    // 뷰 바인딩 정의
    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!

    // 어댑터 인스턴스 생성
    private val recordPhotoAdapter = RecordPhotoAdapter()
    private val recordTextAdapter = RecordTextAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setupBackPressed()
    }

    // 뷰 초기화 및 클릭 이벤트 설정
    private fun initView() {
        val challengeType = ChallengeType.TEXT // TODO: 개발용 임시 타입 설정, 추후 수정 필요

        with(binding.layoutRecordList) {
            // 스크롤 성능 최적화 공통 설정
            rvRecordPhoto.setHasFixedSize(true)
            rvRecordText.setHasFixedSize(true)

            when (challengeType) {
                ChallengeType.PHOTO -> {
                    rvRecordPhoto.run {
                        visibility = View.VISIBLE
                        adapter = recordPhotoAdapter
                        // RecyclerView 데코레이션이 없을 때만 추가하여 중복 적용 방지
                        if (itemDecorationCount == 0) {
                            addItemDecoration(
                                GridSpaceItemDecoration(
                                    spanCount = 3,
                                    spacing = resources.getDimensionPixelSize(R.dimen.record_item_spacing)
                                )
                            )
                        }
                    }
                    rvRecordText.visibility = View.GONE
                }
                ChallengeType.TEXT -> {
                    rvRecordText.run {
                        visibility = View.VISIBLE
                        adapter = recordTextAdapter
                        addItemDecoration(DividerItemDecoration(resources))
                    }
                    rvRecordPhoto.visibility = View.GONE
                }
            }

            // 뒤로가기 버튼 클릭 이벤트
            binding.layoutRecordHeader.btnRecordBack.setOnClickListener {
                handleBackPressed()
            }
        }
    }

    // 시스템 뒤로가기 설정
    private fun setupBackPressed() {
        // 휴대폰 자체의 뒤로가기 동작
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handleBackPressed()
                }
            }
        )
    }

    // 뒤로가기 동작 처리
    private fun handleBackPressed() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}