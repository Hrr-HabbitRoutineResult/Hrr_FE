package com.example.hrr_android.challenge.ui.record

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hrr_android.R
import com.example.hrr_android.challenge.model.ChallengeType
import com.example.hrr_android.challenge.ui.record.adapter.RecordPhotoAdapter
import com.example.hrr_android.challenge.ui.record.adapter.RecordTextAdapter
import com.example.hrr_android.databinding.FragmentRecordBinding


class RecordFragment : Fragment() {
    // 뷰 바인딩 정의
    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!

    // 어댑터 인스턴스 생성
    private val recordPhotoAdapter = RecordPhotoAdapter()
    private val recordTextAdapter = RecordTextAdapter()

    // 사진 인증 목록의 아이템 간격을 설정하는 데코레이션
    private class GridSpaceItemDecoration(
        private val spanCount: Int, // Grid의 column 수
        private val spacing: Int // 아이템 간 간격
    ) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position: Int = parent.getChildAdapterPosition(view)
            val column = position % spanCount

            // 가로 간격 설정 -> 마지막 열을 제외한 모든 아이템의 오른쪽에 간격 추가
            if (column != spanCount - 1) {
                outRect.right = spacing
            }

            // 세로 간격 설정 -> 마지막 행을 제외한 모든 아이템의 아래쪽에 간격 추가
            if (position < state.itemCount - spanCount) {
                outRect.bottom = spacing
            }
        }
    }

    // 글 인증 목록의 구분선을 설정하는 데코레이션
    private class DividerItemDecoration(
        private val resources: Resources
    ) : RecyclerView.ItemDecoration() {
        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val divider = ResourcesCompat.getDrawable(resources, R.drawable.divider_line_1dp, null)

            // 마지막 아이템을 제외한 모든 아이템 사이에 구분선 추가
            for (i in 0 until parent.childCount - 1) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams

                val top = child.bottom + params.bottomMargin
                val bottom = top + (divider?.intrinsicHeight ?: 0)
                val left = parent.paddingLeft
                val right = parent.width - parent.paddingRight

                divider?.setBounds(left, top, right, bottom)
                divider?.draw(c)
            }
        }
    }

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