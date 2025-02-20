package com.example.hrr_android

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.databinding.ActivityMyBadgeDetailBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MyBadgeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyBadgeDetailBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //뷰 바인딩 초기화
        binding = ActivityMyBadgeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로 가기 버튼 클릭 처리
        binding.ivBadgeDetailBack.setOnClickListener{
            finish()
        }


        //데이터 전달 받기
        val gson = Gson()
        val badgeJson = intent.getStringExtra("badgeJson")

        //Badge로 변환
        val badge: Badge? = if (badgeJson != null) {
            gson.fromJson(badgeJson, Badge::class.java)
        } else {
            null
        }

        //뷰 세팅
        if (badge != null) {
            binding.ivObtainedBadge.setImageResource(badge.icon)    // 1. 뱃지 아이콘
            binding.tvObtainedBadgeName.text = badge.name   // 2. 뱃지 이름
            if(!badge.isObtained){
                applyBlackWhiteFilter(binding.ivObtainedBadge)  //미획득 시 흑백 처리
            }
            // 3. 종류 지정
            binding.tvObtainedBadgeType.text = when(badge.type){
                "type" -> "유형"
                "category" -> "카테고리"
                else -> "유형"
            }

            // 4. 획득 조건 바인딩
            val conditionTvs = listOf(binding.tvBadgeCondition01, binding.tvBadgeCondition02, binding.tvBadgeCondition03)
            val conditionCheckIcons = listOf(binding.ivBadgeConditionObtained01, binding.ivBadgeConditionObtained02, binding.ivBadgeConditionObtained03)
            val conditionCls = listOf(binding.clBadgeCondition01, binding.clBadgeCondition02, binding.clBadgeCondition03)


            val badgeId = intent.getIntExtra("badgeId", 0)
            userViewModel.getBadgeCondition(
                badgeId,
                onResult = { conditions ->
                    // 받아온 조건을 UI에 반영
                    conditions.forEachIndexed { index, condition ->
                        if (index < conditionTvs.size) {
                            conditionTvs[index].text = "•  ${condition.description}"
                            conditionTvs[index].visibility = View.VISIBLE

                            conditionCheckIcons[index].setImageResource(
                                if (condition.isAchieved) R.drawable.ic_check_certified_ else R.drawable.ic_check_uncertified_
                            )
                            conditionCheckIcons[index].visibility = View.VISIBLE

                            conditionCls[index].visibility = View.VISIBLE // 조건 레이아웃 표시
                        }
                    }

                    // 5. 남는 뷰 숨기기
                    for (i in conditions.size until conditionTvs.size) {
                        conditionCls[i].visibility = View.GONE
                    }

                })


            userViewModel.errorMessage.observe(this) { errorMsg ->
                errorMsg?.let {
                    val errorToUser = when {
                        it.contains("IllegalStateException") -> "데이터를 불러오는 중 문제가 발생했습니다. 다시 시도해 주세요."
                        it.contains("JsonSyntaxException") -> "서버 응답이 올바르지 않습니다. 업데이트를 확인해 주세요."
                        it.contains("SocketTimeoutException") -> "서버 응답이 지연되고 있습니다. 잠시 후 다시 시도해 주세요."
                        it.contains("IOException") -> "네트워크 연결을 확인해 주세요."
                        else -> "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
                    }

                    Toast.makeText(this, errorToUser, Toast.LENGTH_LONG).show()
                    Log.e("ProfileFragmentVM", "오류 발생: $errorMsg")
                }
            }

//            // 조건에 따라 UI 업데이트
//            badge.obtainCondition.let{obtainCondition ->
//                obtainCondition.forEachIndexed { index, condition ->
//                    if (index < conditionTvs.size) {
//                        conditionTvs[index].text = "•  ${condition.description}"
//                        conditionTvs[index].visibility = View.VISIBLE
//
//                        conditionCheckIcons[index].setImageResource(
//                            if (condition.isObtained) R.drawable.ic_check_certified_ else R.drawable.ic_check_uncertified_
//                        )
//                        conditionCheckIcons[index].visibility = View.VISIBLE
//                    } }
//            }
//
//            // 남는 뷰는 숨김
//            for (i in badge.obtainCondition.size until conditionTvs.size) {
//                conditionCls[i].visibility = View.GONE
//            }

            // 5. 현재 날짜 바인딩
            val today: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            binding.tvBadgeDetailDate.text = "$today 기준"

        }

    }

    //ImageView를 흑백으로 변환
    private fun applyBlackWhiteFilter(imageView: ImageView) {
        val colorMatrix = ColorMatrix()

        //흑백 변환
        colorMatrix.setSaturation(0f)

        // 밝기 조절 (1.0f = 원본, +a = 더 밝게)
        val brightness = 1.1f
        val brightnessMatrix = ColorMatrix(
            floatArrayOf(
                brightness, 0f, 0f, 0f, 20f,  // Red
                0f, brightness, 0f, 0f, 20f,  // Green
                0f, 0f, brightness, 0f, 20f,  // Blue
                0f, 0f, 0f, 1f, 0f  // Alpha
            )
        )

        // 대비 조절 (1.0f = 원본, -a = 연한 흑백)
        val contrast = 0.9f
        val contrastMatrix = ColorMatrix(
            floatArrayOf(
                contrast, 0f, 0f, 0f, 10f, // Red
                0f, contrast, 0f, 0f, 10f, // Green
                0f, 0f, contrast, 0f, 10f, // Blue
                0f, 0f, 0f, 1f, 0f  // Alpha
            )
        )

        // 효과 결합
        colorMatrix.postConcat(brightnessMatrix)
        colorMatrix.postConcat(contrastMatrix)

        // 최종 필터 적용
        val filter = ColorMatrixColorFilter(colorMatrix)
        imageView.colorFilter = filter
    }
}