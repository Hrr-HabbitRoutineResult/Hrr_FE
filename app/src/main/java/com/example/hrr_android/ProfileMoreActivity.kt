package com.example.hrr_android

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.hrr_android.databinding.ActivityProfileMoreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileMoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileMoreBinding    // 뷰 바인딩
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //뷰 바인딩 초기화
        binding = ActivityProfileMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Intent에서 데이터 수신
        val type = intent.getStringExtra("type") ?: ""
        val isMyProfile = intent.getBooleanExtra("isMyProfile", true)

        //클릭이 인식된 부분에 따라 뷰를 다르게 구성
        if (savedInstanceState == null) {
            //제목 변경
            binding.tvProfileMoreTitle.text = when(type){
                "challenge" -> "완주 챌린지"
                "certification" -> "전체 인증기록"
                "badge" -> "마이 뱃지"
                "follower" -> "프로필"
                "following" -> "프로필"
                "setting" -> "설정"
                else -> "완주 챌린지"        //기본적으로 챌린지 화면 사용
            }

            val fragment = when (type) {
                "challenge" -> ProfileChallengeMoreFragment().apply {
                    arguments = Bundle().apply {
                        if(!isMyProfile){
                            val otherId = intent.getIntExtra("ownerId", 0)
                            putInt("ownerId", otherId)
                        }
                    }
                }
                "certification" -> ProfileRecordMoreFragment()
                "badge" -> ProfileBadgeMoreFragment()
                "follower", "following" -> ProfileFollowFragment().apply {
                    arguments = Bundle().apply {
                        putString("selected_tab", type)
                        if(!isMyProfile){
                            val otherId = intent.getIntExtra("ownerId", 0)
                            Log.d("otherDebug", "ProfileMoreActivity - $otherId")
                            putInt("ownerId", otherId)
                        }
                        val myId = intent.getIntExtra("myId", 0)
                        putInt("myId", myId)
                        Log.d("myIdDebug", "ProfileMoreActivity: $myId")
                    }
                }
                "setting" -> SettingFragment()
                else -> ProfileChallengeMoreFragment() // 기본적으로 ProfileChallengeMoreFragment 사용
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_profile_more_fragment_container, fragment)
                .commit()

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fl_profile_more_fragment_container) as? NavHostFragment
            if (navHostFragment != null) {
                navController = navHostFragment.navController
                Log.d("NavDebug", "NavHostFragment 초기화 성공: $navController")
            } else {
                Log.e("NavDebug", "NavHostFragment 초기화 실패: NavHostFragment가 NULL입니다.")
            }
        }

        //뒤로가기 버튼 클릭 처리
        binding.llChallengeMoreTitle.setOnClickListener{
            onBackPressedCallback.handleOnBackPressed()
        }

        // 뒤로 가기 동작 정의
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)


    }

    // 뒤로 가기 동작 정의
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (supportFragmentManager.backStackEntryCount >= 1) {
                supportFragmentManager.popBackStack() // 추가 Fragment가 있을 때느 이전 Fragment로 돌아감

                // 가장 최근에 추가된 Fragment를 다시 보이도록 설정
                val currentFragment = supportFragmentManager.fragments.lastOrNull()
                currentFragment?.let {
                    supportFragmentManager.beginTransaction().show(it).commit()
                }

            } else {
                finish() // Fragment가 1개 뿐이면 Activity 종료
            }
        }
    }

    fun setTitle(title: String){
        binding.tvProfileMoreTitle.text = title
    }
}