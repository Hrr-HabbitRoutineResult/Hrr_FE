package com.example.hrr_android.challenge.ui.record.progress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hrr_android.R
import com.example.hrr_android.challenge.model.ChallengeType
import com.example.hrr_android.challenge.ui.common.decoration.DividerItemDecoration
import com.example.hrr_android.challenge.ui.common.decoration.GridSpaceItemDecoration
import com.example.hrr_android.databinding.FragmentMyProgressBinding
import com.example.hrr_android.challenge.ui.record.adapter.RecordPhotoAdapter
import com.example.hrr_android.challenge.ui.record.adapter.RecordTextAdapter


class MyProgressFragment : Fragment() {
    // 뷰 바인딩 정의
    private var _binding: FragmentMyProgressBinding? = null
    private val binding get() = _binding!!

    // 어댑터 인스턴스 생성
    private val recordPhotoAdapter = RecordPhotoAdapter()
    private val recordTextAdapter = RecordTextAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    // 인증 목록 뷰 설정
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
                        // RecyclerView 데코레이션 중복 적용 방지
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}