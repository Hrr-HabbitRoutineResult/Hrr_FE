package com.example.hrr_android.challenge.certification.ui.post

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.R
import com.example.hrr_android.challenge.certification.ui.post.adapter.CommentAdapter
import com.example.hrr_android.challenge.model.PostResponse
import com.example.hrr_android.challenge.ui.common.decoration.CenterDividerItemDecoration
import com.example.hrr_android.databinding.DialogBottomSheetBinding
import com.example.hrr_android.databinding.FragmentPostBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import com.bumptech.glide.Glide
import java.util.Locale

@AndroidEntryPoint
class PostFragment : Fragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var commentAdapter: CommentAdapter
    private val viewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()

        // 헤더 타이틀 변경
        binding.layoutRecordHeader.tvRecordTitle.text = "게시물"

        // 뒤로가기 버튼 클릭 이벤트
        binding.layoutRecordHeader.btnRecordBack.setOnClickListener {
            handleBackPressed()
        }

        // 더보기 버튼 클릭 이벤트
        binding.layoutPostProfile.btnPostMore.setOnClickListener {
            showBottomSheet()
        }

        // 게시물 데이터 요청
        val verificationId = arguments?.getInt("verification_id") ?: -1
        if (verificationId != -1) {
            viewModel.fetchPostDetail(verificationId)
        }
    }

    private fun setupRecyclerView() {
        if (context == null) return
        commentAdapter = CommentAdapter()
        binding.rvComments.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(CenterDividerItemDecoration(resources))
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.postState.collect { result ->
                result?.onSuccess { post ->
                    updateUI(post)
                }?.onFailure { e ->
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateUI(post: PostResponse) {
        with(binding) {
            // 제목 설정
            tvPostTitle.text = post.title

            // 이미지 설정
            Glide.with(requireContext())
                .load(post.photoUrl)
                .centerCrop()
                .into(ivPhotoPreview)

            // 내용 설정
            post.content?.let {
                tvPostContent.text = it
                tvPostContent.visibility = View.VISIBLE
            } ?: run {
                tvPostContent.visibility = View.GONE
            }

            // 게시물 프로필 설정
            with(layoutPostProfile) {
                tvPostTime.text = post.created_at.formatToDisplayDate()
            }

            // 댓글 설정
            commentAdapter.setItems(post.comment)
        }
    }

    // 날짜 포맷 변환 확장 함수
    private fun String.formatToDisplayDate(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
        return try {
            val date = inputFormat.parse(this)
            outputFormat.format(date ?: return this)
        } catch (e: Exception) {
            this
        }
    }

    private fun showBottomSheet() {
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

        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        val dialogBinding = DialogBottomSheetBinding.inflate(layoutInflater)

        dialogBinding.apply {
            tvBlock.apply {
                text = "수정"
                setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
                setOnClickListener {
                    // TODO: 수정 처리
                    bottomSheetDialog.dismiss()
                }
            }

            tvReport.apply {
                text = "삭제"
                setOnClickListener {
                    // TODO: 삭제 처리
                    bottomSheetDialog.dismiss()
                }
            }

            tvCancel.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }

        // 다이얼로그가 닫힐 때 배경도 함께 제거
        bottomSheetDialog.setOnDismissListener {
            (binding.root as ViewGroup).removeView(dimView)
        }

        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
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