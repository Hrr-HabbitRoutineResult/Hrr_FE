package com.example.hrr_android.onboarding.utils

import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.example.hrr_android.onboarding.ui.OnboardingActivity

object RadioGroupUtils {

    fun setupRadioGroup(radioGroup: RadioGroup, fragment: Fragment) {
        for (i in 0 until radioGroup.childCount) {
            val child = radioGroup.getChildAt(i)
            if (child is LinearLayout) {
                // LinearLayout 내부의 RadioButton 처리
                for (j in 0 until child.childCount) {
                    val button = child.getChildAt(j)
                    if (button is RadioButton) {
                        button.setOnClickListener {
                            // 선택된 버튼만 활성화, 나머지는 비활성화
                            updateRadioGroupSelection(radioGroup, button)
                            updateNextButtonState(fragment)
                        }
                    }
                }
            } else if (child is RadioButton) {
                child.setOnClickListener {
                    // 선택된 버튼만 활성화, 나머지는 비활성화
                    updateRadioGroupSelection(radioGroup, child)
                    updateNextButtonState(fragment)
                }
            }
        }
    }

    private fun updateRadioGroupSelection(radioGroup: RadioGroup, selectedButton: RadioButton) {
        for (i in 0 until radioGroup.childCount) {
            val child = radioGroup.getChildAt(i)
            if (child is LinearLayout) {
                // LinearLayout 내부의 RadioButton 처리
                for (j in 0 until child.childCount) {
                    val button = child.getChildAt(j)
                    if (button is RadioButton) {
                        button.isSelected = (button == selectedButton)
                    }
                }
            } else if (child is RadioButton) {
                child.isSelected = (child == selectedButton)
            }
        }
    }

    // 라디오 그룹에서 적어도 하나의 버튼이 선택되었는지를 반환
    fun isRadioGroupValid(radioGroup: RadioGroup): Boolean {
        for (i in 0 until radioGroup.childCount) {
            val child = radioGroup.getChildAt(i)
            if (child is LinearLayout) {
                // LinearLayout 내부의 RadioButton 처리
                for (j in 0 until child.childCount) {
                    val button = child.getChildAt(j)
                    if (button is RadioButton && button.isChecked) {
                        return true
                    }
                }
            } else if (child is RadioButton) {
                if (child.isChecked) {
                    return true
                }
            }
        }
        return false
    }

    private fun updateNextButtonState(fragment: Fragment) {
        (fragment.activity as? OnboardingActivity)?.updateNextButtonState(fragment)
    }

    // 최대 n개의 RadioButton만 선택 가능하도록 설정
    fun setupRadioGroupWithMax(radioGroup: RadioGroup, maxSelection: Int, fragment: Fragment) {
        for (i in 0 until radioGroup.childCount) {
            val child = radioGroup.getChildAt(i)
            if (child is LinearLayout) {
                for (j in 0 until child.childCount) {
                    val button = child.getChildAt(j)
                    if (button is RadioButton) {
                        button.setOnClickListener {
                            handleMaxSelection(radioGroup, button, maxSelection)
                            updateNextButtonState(fragment)
                        }
                    }
                }
            } else if (child is RadioButton) {
                child.setOnClickListener {
                    handleMaxSelection(radioGroup, child, maxSelection)
                    updateNextButtonState(fragment)
                }
            }
        }
    }

    private fun handleMaxSelection(radioGroup: RadioGroup, selectedButton: RadioButton, maxSelection: Int) {
        val selectedButtons = mutableListOf<RadioButton>()

        // 현재 선택된 버튼들을 추적
        for (i in 0 until radioGroup.childCount) {
            val child = radioGroup.getChildAt(i)
            if (child is LinearLayout) {
                for (j in 0 until child.childCount) {
                    val button = child.getChildAt(j)
                    if (button is RadioButton && button.isSelected) {
                        selectedButtons.add(button)
                    }
                }
            } else if (child is RadioButton) {
                if (child.isSelected) {
                    selectedButtons.add(child)
                }
            }
        }

        // 버튼 선택 처리
        if (!selectedButton.isSelected) {
            if (selectedButtons.size < maxSelection) {
                selectedButton.isSelected = true
            }
        } else {
            selectedButton.isSelected = false
        }
    }

    // 선택된 버튼이 있는지 확인하는 함수
    fun hasSelectedButtons(radioGroup: RadioGroup): Boolean {
        for (i in 0 until radioGroup.childCount) {
            val child = radioGroup.getChildAt(i)
            if (child is LinearLayout) {
                for (j in 0 until child.childCount) {
                    val button = child.getChildAt(j)
                    if (button is RadioButton && button.isSelected) {
                        return true
                    }
                }
            } else if (child is RadioButton) {
                if (child.isSelected) {
                    return true
                }
            }
        }
        return false
    }
}
