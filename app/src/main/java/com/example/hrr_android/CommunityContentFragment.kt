package com.example.hrr_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.FragmentCommunityContentBinding

class CommunityContentFragment : Fragment() {

    private var _binding: FragmentCommunityContentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCommunityPostUpload.setOnClickListener {
            // 추후에 구현 예정
            Toast.makeText(requireContext(), "게시글 작성으로 이동", Toast.LENGTH_SHORT).show()
        }

        // 더미 데이터 생성
        val posts = mutableListOf(
            Post("해커스 공기업 NCS 통합 봉모 주황이 1회차", "모르는 문제 있는데 채팅 받아줄 사람?", "작성자 1", 10, 5, "2025-01-01", ""),
            Post("정출연 중에서 어디가 제일 좋다고 생각해?", "건보 자소서가 중요하다고 하는데 건보가 원하는 방향성을 모르겠어 조금만 더 길게 작성하기 위해 아무 말이나 합니다..", "작성자 2", 0, 0, "2025-01-02", ""),
            Post("게시글 3", "내용 3", "작성자 3", 7, 0, "2025-01-03", ""),
            Post("게시글 4", "내용 4", "작성자 4", 0, 3, "2025-01-04", ""),
            Post("게시글 5", "내용 5", "작성자 5", 3, 0, "2025-01-05", ""),
            Post("게시글 6", "내용 6", "작성자 6", 4, 3, "2025-01-06", ""),
            Post("게시글 7", "내용 7", "작성자 7", 7, 6, "2025-01-07", ""),
            Post("게시글 8", "내용 8", "작성자 8", 0, 0, "2025-01-08", "")
        )

        // 어댑터 설정
        val adapter = PostRVAdapter(posts, { post ->
            Toast.makeText(requireContext(), "${post.title} 클릭됨", Toast.LENGTH_SHORT).show()
        }, "CommunityContentFragment") // 여기에서 프래그먼트 구분값 전달

        // RecyclerView 설정
        binding.rvBoardPostList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBoardPostList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
