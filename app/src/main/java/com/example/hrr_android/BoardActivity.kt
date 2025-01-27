package com.example.hrr_android

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.databinding.ActivityBoardBinding

class BoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardBinding // View Binding 객체

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View Binding 초기화
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로부터 데이터 수신
        val baseCategory = intent.getStringExtra("baseCategory") ?: "Default Title"
        val subCategory = intent.getStringExtra("subTitle") ?: ""
        val fragmentName = intent.getStringExtra("fragment") ?: "CommunityContentFragment"

        // 기본 게시판 제목 설정
        binding.tvBoardTitle.text = baseCategory

        // 소게시판 제목 설정 (소게시판이 없을 경우 숨김 처리)
        if (subCategory.isNotEmpty()) {
            binding.tvBoardDescribe.text = subCategory
            binding.tvBoardDescribe.visibility = View.VISIBLE
        } else {
            binding.tvBoardDescribe.visibility = View.GONE
        }

        // Fragment 추가 로직
        if (savedInstanceState == null) {
            val fragment = when (fragmentName) {
                "CommunityTabContentFragment" -> CommunityTabContentFragment()
                else -> CommunityContentFragment() // 기본적으로 CommunityContentFragment 사용
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.layout_board_fragment_container, fragment) // FragmentContainerView에 선택된 Fragment 추가
                .commit()
        }

        // 뒤로 가기 버튼 클릭 리스너 설정
        binding.btnBoardBack.setOnClickListener {
            finish()
        }
    }
}

