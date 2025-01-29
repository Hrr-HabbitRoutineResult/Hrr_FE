package com.example.hrr_android

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.databinding.ActivityMyBadgeDetailBinding

class MyBadgeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyBadgeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //뷰 바인딩 초기화
        binding = ActivityMyBadgeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뷰 세팅
        binding.ivObtainedBadge.setImageResource(intent.getIntExtra("icon", R.drawable.badge_category_exercise_master)) //뱃지 아이콘
        binding.tvObtainedBadgeName.text = intent.getStringExtra("name")    //뱃지 이름

        if(!intent.getBooleanExtra("isObtained", false)){
            applyBlackWhiteFilter(binding.ivObtainedBadge)  //미획득 시 흑백 처리
        }

        //종류 지정
        binding.tvObtainedBadgeType.text = when(intent.getStringExtra("type")){
            "type" -> "유형"
            "category" -> "카테고리"
            else -> "유형"
        }


        //TODO: 획득 조건 바인딩

    }

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