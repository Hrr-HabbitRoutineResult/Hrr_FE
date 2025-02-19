package com.example.hrr_android.challenge.certification.ui.draft

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.R
import com.example.hrr_android.challenge.certification.ui.draft.adapter.DraftListAdapter
import com.example.hrr_android.challenge.ui.common.decoration.CenterDividerItemDecoration
import com.example.hrr_android.databinding.FragmentDraftListBinding


class DraftListFragment : Fragment() {
    private var _binding: FragmentDraftListBinding? = null
    private val binding get() = _binding!!
    private val draftListAdapter = DraftListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDraftListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutRecordHeader.tvRecordTitle.text = "임시저장"

        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        binding.rvDrafts.apply {
            adapter = draftListAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(CenterDividerItemDecoration(resources))
        }
        // 선택 개수 변경 리스너 설정
        draftListAdapter.onSelectedCountChanged = { count ->
            binding.tvDraftCount.text = if (count > 0) "총 ${count}개 선택" else "총 0개 선택"
        }

    }

    private fun setupClickListeners() {
        binding.apply {
            // 뒤로가기 버튼
            layoutRecordHeader.btnRecordBack.setOnClickListener {
                findNavController().navigateUp()
            }

            // 편집 모드 버튼
            btnDraftEdit.setOnClickListener {
                enableEditMode(true)
            }

            // 선택 삭제 버튼
            btnDeleteSelected.setOnClickListener {
                // 선택된 항목 삭제 처리
                showDeleteConfirmDialog()
            }

            // 전체 삭제 버튼
            btnDeleteAll.setOnClickListener {
                // 전체 삭제 처리
                showDeleteConfirmDialog()
            }
        }
    }

    private fun enableEditMode(enabled: Boolean) {
        binding.apply {
            btnDraftEdit.visibility = if (enabled) View.GONE else View.VISIBLE
            llEditButtons.visibility = if (enabled) View.VISIBLE else View.GONE

            tvDraftCount.text = if (enabled) "총 0개 선택" else "글 3개"

            draftListAdapter.setEditMode(enabled)
        }
    }

    private fun showDeleteConfirmDialog() {
        // 어두운 배경 뷰 생성
        val dimView = View(requireContext()).apply {
            setBackgroundColor(Color.parseColor("#80000000"))
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        // 프래그먼트 루트 뷰에 어두운 배경 추가
        (binding.root as ViewGroup).addView(dimView)

        val dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_default)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // 다이얼로그가 취소될 때 배경도 함께 제거
            setOnDismissListener {
                (binding.root as ViewGroup).removeView(dimView)
            }
        }

        dialog.findViewById<TextView>(R.id.tv_dialog_title).text = "임시저장 글 삭제"

        dialog.findViewById<TextView>(R.id.tv_dialog_content).text =
            "선택된 임시저장 글을 삭제하실 건가요?\n" + "삭제된 글은 다시 볼 수 없습니다."

        dialog.findViewById<TextView>(R.id.tv_dialog_yes).apply {
            text = "삭제"
            setOnClickListener {
                // TODO: 실제 삭제 로직 구현 필요
                dialog.dismiss()
            }
        }

        dialog.findViewById<TextView>(R.id.tv_dialog_no).apply {
            text = "취소"
            setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}