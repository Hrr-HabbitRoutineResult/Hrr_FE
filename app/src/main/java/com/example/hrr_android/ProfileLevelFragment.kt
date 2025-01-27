package com.example.hrr_android

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.hrr_android.databinding.FragmentProfileLevelBinding

class ProfileLevelFragment : Fragment() {
    private var _binding: FragmentProfileLevelBinding? = null
    private val binding get() = _binding!!
    private var myPoint: Int = 150

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //레벨에 따라 아이콘 상태(텍스트, 배경)를 설정
        initLevelIcon()

        //포인트 바인딩
        binding.tvLevelMypoint.text = "${myPoint}P"

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        Log.d("levelDebug", "2")

        // 레벨 순서 정의
        val levels = listOf("Normal","Bronze", "Silver", "Gold", "Master", "Challenger")
        val currentLevelIndex = levels.indexOf(level)

        //레벨에 따른 아이콘과 텍스트 매칭("일반"은 아이콘 없으므로 제외, "챌린저"는 별도 로직으로 처리)
        val iconAndText = mapOf(
            "Bronze" to Pair(binding.ivLevelBShape, binding.tvLevelB),
            "Silver" to Pair(binding.ivLevelSShape, binding.tvLevelS),
            "Gold" to Pair(binding.ivLevelGShape, binding.tvLevelG),
            "Master" to Pair(binding.ivLevelMShape, binding.tvLevelM),
            "Challenger" to Pair(binding.ivLevelFinalShape, binding.ivLevelFinal)
        )

        Log.d("levelDebug", "3")
        // 달성된 레벨까지만 아이콘 상태 업데이트
        var levelIndex: Int = 0
        for ((levelName, pair) in iconAndText) {
            Log.d("levelDebug", levelName)
            val bg = pair.first
            val inner = pair.second

            levelIndex = levels.indexOf(levelName)  //브론즈~챌린저(1~5)

            // 현재 레벨 전까지는 달성 상태로 변경
            if (levelIndex < currentLevelIndex) {
                Log.d("levelDebug", "5")
                changeIcon(bg, inner, requireContext(), R.drawable.bg_level_map_achieved, R.color.white)
            }else if(levelIndex == currentLevelIndex){
                // 현재 레벨은 최초 달성 여부 판단 후 변경
                val prefs = requireContext().getSharedPreferences("Level_first", Context.MODE_PRIVATE)
                val isFirstAchieved = prefs.getBoolean("Level_$levelName", true)
                Log.d("levelDebug", "6")

                if(isFirstAchieved){
                    Log.d("levelDebug", "7")
                    //그라데이션 배경
                    changeIcon(bg, inner, requireContext(), R.drawable.bg_level_map_achieved_first, R.color.sub_03)
                    Log.d("levelDebug", "8")

                    //레벨 달성 바 변경
                    binding.llLevelAchieveBar.setBackgroundResource(R.drawable.bg_radius30_sub06)
                    binding.ivLevelCheck.setImageResource(R.drawable.ic_level_achieved)
                    binding.tvLevelAchievedDatail.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))

                    //최초 달성 여부를 내부 데이터에 저장
                    prefs.edit().putBoolean("Level_$levelName", false).apply()
                }
                else{
                    changeIcon(bg, inner, requireContext(), R.drawable.bg_level_map_achieved, R.color.white)
                }
            }

            ////다이얼로그 띄우기
            //아이콘 배경의 리소스 id를 전달하여 각각 다르게 구현
            bg.setOnClickListener {
                val bgDrawableResId = bg.tag as? Int ?: R.drawable.bg_level_map_default
//                Log.d("levelDebug", "Drawable ResId: $bgDrawableResId")
//                when (bgDrawableResId) {
//                    R.drawable.bg_level_map_default -> Log.d("levelDebug", "Default Action")
//                    R.drawable.bg_level_map_achieved_first -> Log.d("levelDebug", "Achieved First Action")
//                    R.drawable.bg_level_map_achieved -> Log.d("levelDebug", "Achieved Action")
//                }

                // 뷰의 id 이름 가져오기
                val viewIdName = resources.getResourceEntryName(inner.id) // e,g, "tv_level_s"
                // 핵심 부분 추출
                val levelChar = viewIdName.substringAfterLast("_") // "_" 뒤의 문자 추출 e.g. "s"



                //다이얼로그 호출
                val dialog = LevelDialog.newInstance(bgDrawableResId, levelChar)
                dialog.setListener(object : LevelDialogInterface {
                    override fun onGetButtonClick() {
                        //메시지 출력
                        Toast.makeText(requireContext(), "$levelName 레벨을 달성하였습니다. 축하합니다!", Toast.LENGTH_SHORT).show()

                        //레벨 아이콘 "달성 완료" 상태로 변경
                        changeIcon(bg, inner, requireContext(), R.drawable.bg_level_map_achieved, R.color.white)

                        //레벨 달성 바 기본 상태로 되돌리기
                        binding.llLevelAchieveBar.setBackgroundResource(R.drawable.bg_radius_30_grey_50)
                        binding.ivLevelCheck.setImageResource(R.drawable.ic_level_default)
                        binding.tvLevelAchievedDatail.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_500))

                        //Todo: "최초 여부" 상태 업데이트

                    }
                })
                dialog.show(parentFragmentManager, "LevelDialog")
            }

        }
        Log.d("levelDebug", "4")

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
                inner.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.sub_03))
            }
            else -> {
                // 처리하지 않음
            }
        }
    }


}