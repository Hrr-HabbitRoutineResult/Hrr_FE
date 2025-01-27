package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.FragmentCommunityContentBinding
import com.example.hrr_android.databinding.FragmentCommunityTabContentBinding


class CommunityTabContentFragment : Fragment() {

    private var _binding: FragmentCommunityTabContentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityTabContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCommunityTapPostUpload.setOnClickListener {
            // 추후에 구현 예정
            Toast.makeText(requireContext(), "게시글 작성으로 이동", Toast.LENGTH_SHORT).show()
        }

        // 더미 데이터 생성
        val posts = mutableListOf(
            Post("어제 PT 갔거든? 근데 피티쌤이", "나보고 어제 피티 끝나고 족발 먹었냐는 거야..", "작성자 1", 10, 5, "2025-01-01", "운동게시판"),
            Post("교수님이 아무래도 내가 자기 수업만 듣는 줄 아는 듯", "그렇지 않고서 이렇게 많은 과제를 줄 수는 없어.....", "작성자 2", 0, 0, "2025-01-02", "학업게시판"),
            Post("게시글 3", "내용 3", "작성자 3", 7, 0, "2025-01-03", "공공기관/공무원/정출연 취준생"),
            Post("게시글 4", "내용 4", "작성자 4", 0, 3, "2025-01-04", "생활습관게시판"),
            Post("게시글 5", "내용 5", "작성자 5", 3, 0, "2025-01-05", "취업준비게시판"),
            Post("게시글 6", "내용 6", "작성자 6", 4, 3, "2025-01-06", "스터디게시판"),
            Post("게시글 7", "내용 7", "작성자 7", 7, 6, "2025-01-07", "취미게시판"),
            Post("게시글 8", "내용 8", "작성자 8", 0, 0, "2025-01-08", "학업게시판")
        )

        // 어댑터 설정
        val adapter = PostRVAdapter(posts, { post ->
            Toast.makeText(requireContext(), "${post.title} 클릭됨", Toast.LENGTH_SHORT).show()
        }, "CommunityTabContentFragment") // 여기에서 프래그먼트 구분값 전달

        // RecyclerView 설정
        binding.rvBoardPostList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBoardPostList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}