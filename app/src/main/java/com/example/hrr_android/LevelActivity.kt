package com.example.hrr_android

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.hrr_android.databinding.ActivityLevelBinding
import android.content.res.ColorStateList
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class LevelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLevelBinding     // 뷰 바인딩
    private var myPoint: Int = 0        //현재 획득한 포인트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바인딩 초기화
        binding = ActivityLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // point 연동
        myPoint = intent.getIntExtra("point", 0)

        //X 버튼 클릭 리스너
        binding.ivLevelClose.setOnClickListener {
            finish()
        }

        //레벨에 따라 아이콘 상태(텍스트, 배경)를 설정
        initLevelIcon()

        //내 포인트 바인딩
        binding.tvLevelMypoint.text = "${myPoint}P"
    }

    private fun initLevelIcon(){
        //포인트에 따른 레벨 정의
        val level = when {
            myPoint < 30 -> "Normal"
            myPoint < 80 -> "Bronze"
            myPoint < 150 -> "Silver"
            myPoint < 250 -> "Gold"
            myPoint < 400 -> "Master"
            else -> "Challenger"
        }

        // 레벨 순서 정의 - 추후 매칭을 위함
        val levels = listOf("Normal","Bronze", "Silver", "Gold", "Master", "Challenger")
        val currentLevelIndex = levels.indexOf(level)

        //레벨에 따른 아이콘과 텍스트 매칭("일반"은 아이콘 없으므로 제외)
        val iconAndText = mapOf(
            "Bronze" to Triple(binding.ivLevelBShape, binding.tvLevelB, 30),
            "Silver" to Triple(binding.ivLevelSShape, binding.tvLevelS, 80),
            "Gold" to Triple(binding.ivLevelGShape, binding.tvLevelG, 150),
            "Master" to Triple(binding.ivLevelMShape, binding.tvLevelM, 250),
            "Challenger" to Triple(binding.ivLevelFinalShape, binding.ivLevelFinal, 400)
        )

        var nextLevel = Pair(0, "")     //달성할 다음 레벨 정보를 저장하기 위한 "세미 전역변수"
        var isFirstAchieved = true      //최초 달성 여부를 사용하기 위한 "세미 전역변수"

        // 달성된 레벨까지만 아이콘 상태 업데이트
        for ((levelName, triple) in iconAndText) {
            val bg = triple.first           //아이콘 배경
            val inner = triple.second       //아이콘 내부(텍스트 or 로고)
            val levelPoint = triple.third   //해당 레벨 달성 위한 기준 포인트

            val levelIndex = levels.indexOf(levelName)  //브론즈~챌린저(1~5)

            // 현재 레벨 전까지는 달성 상태로 변경
            if (levelIndex < currentLevelIndex) {
                changeIcon(bg, inner, this, R.drawable.bg_level_map_achieved, R.color.white)
            }
            else if(levelIndex == currentLevelIndex){
                // 현재 레벨은 최초 달성 여부 판단 후 변경
                val prefs = this.getSharedPreferences("Level_first", Context.MODE_PRIVATE)
                isFirstAchieved = prefs.getBoolean("Level_$levelName", true)

                //최초 달성 시
                if(isFirstAchieved){
                    //그라데이션 배경으로 설정
                    changeIcon(bg, inner, this, R.drawable.bg_level_map_achieved_first, R.color.sub_03)

                    //레벨 달성 바 변경
                    binding.llLevelAchieveBar.setBackgroundResource(R.drawable.bg_radius30_sub06)
                    binding.ivLevelCheck.setImageResource(R.drawable.ic_level_achieved)
                    binding.tvLevelAchievedDatail.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
                    binding.tvLevelAchievedDatail.text = "포인트 ${levelPoint}P 달성 : $levelName 획득!"
                }
                else{
                    //달성 완료 상태로 설정
                    changeIcon(bg, inner, this, R.drawable.bg_level_map_achieved, R.color.white)
                }
            }else if(levelIndex == currentLevelIndex + 1){
                //다음 단계 달성 전의 텍스트 설정을 위한 if문
                nextLevel = Pair(levelPoint, levelName)

                if(!isFirstAchieved){
                    //최초 달성 시 다음 단계 달성 멘트로 오버되는 거 방지
                    binding.tvLevelAchievedDatail.text = "포인트 ${levelPoint}P 달성 : $levelName 획득!"
                }

            }

            ////다이얼로그 띄우기
            //아이콘 배경의 리소스 id를 전달하여 각각 다르게 구현
            bg.setOnClickListener {
                val bgDrawableResId = bg.tag as? Int ?: R.drawable.bg_level_map_default

                // 뷰의 id 이름 가져오기
                val viewIdName = resources.getResourceEntryName(inner.id) // e,g, "tv_level_s"
                // 핵심 부분 추출
                val levelChar = viewIdName.substringAfterLast("_") // "_" 뒤의 문자 추출 e.g. "s"



                //다이얼로그 호출
                val dialog = LevelDialog.newInstance(bgDrawableResId, levelChar)
                dialog.setListener(object : LevelDialogInterface {
                    override fun onGetButtonClick() {
                        //메시지 출력
                        Toast.makeText(this@LevelActivity, "$levelName 레벨을 달성하였습니다. 축하합니다!", Toast.LENGTH_SHORT).show()

                        //레벨 아이콘 "달성 완료" 상태로 변경
                        changeIcon(bg, inner, this@LevelActivity, R.drawable.bg_level_map_achieved, R.color.white)

                        //레벨 달성 바 기본 상태로 되돌리기 - 챌린저 도달 시에는 변화 없음
                        if(levelChar != "final"){
                            binding.llLevelAchieveBar.setBackgroundResource(R.drawable.bg_radius_30_grey_50)
                            binding.ivLevelCheck.setImageResource(R.drawable.ic_level_default)
                            binding.tvLevelAchievedDatail.setTextColor(ContextCompat.getColor(this@LevelActivity, R.color.grey_500))
                            binding.tvLevelAchievedDatail.text = "포인트 ${nextLevel.first}P 달성 : ${nextLevel.second} 획득!"     //nextLevel = Pair(levelPoint, levelName)
                        }else{
                            //챌린저 도달 시 뷰 수정
                            binding.tvLevelMainTitle.text = "모든 레벨을 완주했어요!"
                            binding.tvLevelSubTitle.text = "이제 어떤 자기개발이든 모두 해내실 수 있을 거예요"
                        }

                        //Todo: "최초 여부" 상태 업데이트(api)
                        //최초 달성 여부를 내부 데이터에 저장
                        val prefs = this@LevelActivity.getSharedPreferences("Level_first", Context.MODE_PRIVATE)
                        prefs.edit().putBoolean("Level_$levelName", false).apply()

                    }
                })
                dialog.show(supportFragmentManager, "LevelDialog")
            }

        }

    }

    private fun changeIcon(bg: ImageView, inner: View, context: Context, backgroundRes: Int, innerColorRes: Int){
        // 배경 색상 변경
        bg.setImageResource(backgroundRes)
        bg.tag = backgroundRes

        // 텍스트 색상 변경
        when (inner) {
            is TextView -> {
                // 텍스트 색상 변경
                inner.setTextColor(ContextCompat.getColor(context, innerColorRes))
            }
            is ImageView -> {
                // 내부 이미지 색상 변경
                inner.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, innerColorRes))
            }
            else -> {
                // 처리하지 않음
            }
        }
    }
}