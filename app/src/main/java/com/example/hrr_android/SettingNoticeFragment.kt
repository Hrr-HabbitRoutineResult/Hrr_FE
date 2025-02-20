package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.example.hrr_android.databinding.FragmentSettingNoticeBinding

class SettingNoticeFragment : Fragment() {
    private var _binding: FragmentSettingNoticeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingNoticeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        redrawAllSwitch()

        initClickListener()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (parentFragmentManager.backStackEntryCount >= 1) {
                        parentFragmentManager.popBackStack() // 추가 Fragment가 있을 때느 이전 Fragment로 돌아감

                        // 가장 최근에 추가된 Fragment를 다시 보이도록 설정
                        val currentFragment = parentFragmentManager.fragments.lastOrNull()
                        currentFragment?.let {
                            parentFragmentManager.beginTransaction().show(it).commit()
                        }
                    }

                    (activity as? ProfileMoreActivity)?.setTitle("설정")
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? ProfileMoreActivity)?.setTitle("설정")
        _binding = null
    }

    private fun redrawSwitch(switchCompat: SwitchCompat) {
        val parent = switchCompat.parent as? ConstraintLayout ?: return
        val constraintSet = ConstraintSet()

        // ConstraintLayout 내부의 모든 View가 ID를 가지고 있는지 확인 - clone 시 모든 View에 ID가 있어야 함
        parent.children.forEach { child ->
            if (child.id == View.NO_ID) {
                child.id = View.generateViewId() // 자동 ID 할당
            }
        }

        constraintSet.clone(parent) // 기존 Constraint 복사

        // 기존 SwitchCompat 제거
        parent.removeView(switchCompat)

        // 새로운 SwitchCompat 생성
        val newSwitch = SwitchCompat(parent.context).apply {
            id = switchCompat.id // 기존 ID 유지

            trackDrawable = ContextCompat.getDrawable(context, R.drawable.toggle_bg)
            layoutParams = switchCompat.layoutParams
        }

        parent.addView(newSwitch) // 새로운 SwitchCompat 추가

        // addView()는 기존의 Constraint 사라지므로 다시 적용
        parent.post {
            constraintSet.applyTo(parent)
        }
    }

    private fun redrawAllSwitch(){
        val switches = listOf(
            binding.switchAllNoticeStop,
            binding.switchCommentForPost,
            binding.switchNewMesssage,
            binding.switchNewBadge,
            binding.switchNewFollower)
        switches.forEach{redrawSwitch(it)}
    }

    private fun initClickListener() {
        // 클릭 처리
        // 계정 설정
        binding.llSettingPostComment.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                // 현재 보여지고 있는 Fragment 숨기기
                parentFragmentManager.findFragmentById(R.id.fl_profile_more_fragment_container)?.let { hide(it) }

                // 새로운 Fragment 추가
                add(R.id.fl_profile_more_fragment_container, SettingNoticeMoreFragment())
                addToBackStack(null) // 뒤로 가기 지원
                commit()
            }

            (activity as? ProfileMoreActivity)?.setTitle("챌린지 및 인증")

        }
    }

}