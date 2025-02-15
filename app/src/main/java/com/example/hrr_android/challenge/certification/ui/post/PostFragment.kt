package com.example.hrr_android.challenge.certification.ui.post

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.R
import com.example.hrr_android.challenge.certification.ui.post.adapter.CommentAdapter
import com.example.hrr_android.databinding.FragmentPostBinding


class PostFragment : Fragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var commentAdapter: CommentAdapter

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
        // 헤더 타이틀 변경
        binding.layoutRecordHeader.tvRecordTitle.text = "게시물"

        // 뒤로가기 버튼 클릭 이벤트
        binding.layoutRecordHeader.btnRecordBack.setOnClickListener {
            handleBackPressed()
        }
    }

    private fun setupRecyclerView() {
        if (context == null) return
        commentAdapter = CommentAdapter()
        binding.rvComments.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
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