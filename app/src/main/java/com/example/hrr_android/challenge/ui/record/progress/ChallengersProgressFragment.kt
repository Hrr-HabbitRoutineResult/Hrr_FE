package com.example.hrr_android.challenge.ui.record.progress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.R
import com.example.hrr_android.challenge.model.ChallengeType
import com.example.hrr_android.challenge.ui.common.decoration.DividerItemDecoration
import com.example.hrr_android.challenge.ui.common.decoration.GridSpaceItemDecoration
import com.example.hrr_android.challenge.ui.record.adapter.RecordPhotoAdapter
import com.example.hrr_android.challenge.ui.record.adapter.RecordTextAdapter
import com.example.hrr_android.databinding.FragmentChallengersProgressBinding


class ChallengersProgressFragment : Fragment() {
    // 뷰 바인딩 정의
    private var _binding: FragmentChallengersProgressBinding? = null
    private val binding get() = _binding!!

    // 어댑터 인스턴스 생성
    private val recordPhotoAdapter = RecordPhotoAdapter()
    private val recordTextAdapter = RecordTextAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChallengersProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    // 뷰 초기화 및 클릭 이벤트 설정
    private fun initView() {
        // TODO: 개발용 월요일 버튼 클릭 이벤트 설정, 추후 수정 필요
        binding.layoutChallengersOverview.root.findViewById<View>(R.id.ll_btn_monday)
            ?.setOnClickListener {
                updateLayout()
            }
    }

    // 레이아웃 상태 업데이트
    private fun updateLayout() {
        // 프로그레스 컨테이너 숨기기
        binding.llChallengersProgressContainer.visibility = View.GONE

        // 구분선, 인증 목록 표시
        binding.viewDivider.visibility = View.VISIBLE
        binding.layoutRecordList.root.visibility = View.VISIBLE

        // overview 타이틀 영역 색상 변경
        binding.layoutChallengersOverview.root.run {
            findViewById<TextView>(R.id.tv_overview_title)?.setTextColor(
                ContextCompat.getColor(context, R.color.grey_500)
            )
            findViewById<ImageView>(R.id.btn_calendar)?.setColorFilter(
                ContextCompat.getColor(context, R.color.grey_500)
            )
        }

        // RecyclerView 설정
        initRecordList(binding.layoutRecordList.root)
    }

    // RecyclerView 초기화 및 설정
    private fun initRecordList(view: View) {
        val challengeType = ChallengeType.PHOTO // TODO: 개발용 임시 타입 설정, 추후 수정 필요

        with(view) {
            val rvRecordPhoto = findViewById<RecyclerView>(R.id.rv_record_photo)
            val rvRecordText = findViewById<RecyclerView>(R.id.rv_record_text)

            // 스크롤 성능 최적화 공통 설정
            rvRecordPhoto.setHasFixedSize(true)
            rvRecordText.setHasFixedSize(true)

            when (challengeType) {
                ChallengeType.PHOTO -> {
                    rvRecordPhoto.run {
                        visibility = View.VISIBLE
                        adapter = recordPhotoAdapter
                        layoutManager = GridLayoutManager(context, 3)
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
                        layoutManager = LinearLayoutManager(context)
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