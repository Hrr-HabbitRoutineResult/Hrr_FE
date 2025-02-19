package com.example.hrr_android.onboarding.utils

import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.example.hrr_android.onboarding.ui.OnboardingActivity

object RadioGroupUtils {

    fun setupRadioGroup(radioGroup: RadioGroup, fragment: Fragment) {
        processRadioButtons(radioGroup) { button ->
            button.setOnClickListener {
                updateRadioGroupSelection(radioGroup, button)
                updateNextButtonState(fragment)
            }
        }
    }

    private fun updateRadioGroupSelection(radioGroup: RadioGroup, selectedButton: RadioButton) {
        processRadioButtons(radioGroup) { button ->
            button.isSelected = (button == selectedButton)
        }
    }

    fun isRadioGroupValid(radioGroup: RadioGroup): Boolean {
        var isValid = false
        processRadioButtons(radioGroup) { button ->
            if (button.isChecked) isValid = true
        }
        return isValid
    }

    private fun updateNextButtonState(fragment: Fragment) {
        (fragment.activity as? OnboardingActivity)?.updateNextButtonState(fragment)
    }

    fun setupRadioGroupWithMax(radioGroup: RadioGroup, maxSelection: Int, fragment: Fragment) {
        processRadioButtons(radioGroup) { button ->
            button.setOnClickListener {
                handleMaxSelection(radioGroup, button, maxSelection)
                updateNextButtonState(fragment)
            }
        }
    }

    private fun handleMaxSelection(radioGroup: RadioGroup, selectedButton: RadioButton, maxSelection: Int) {
        val selectedButtons = mutableListOf<RadioButton>()

        processRadioButtons(radioGroup) { button ->
            if (button.isSelected) selectedButtons.add(button)
        }

        if (!selectedButton.isSelected) {
            if (selectedButtons.size < maxSelection) selectedButton.isSelected = true
        } else {
            selectedButton.isSelected = false
        }
    }

    fun hasSelectedButtons(radioGroup: RadioGroup): Boolean {
        var hasSelection = false
        processRadioButtons(radioGroup) { button ->
            if (button.isSelected) hasSelection = true
        }
        return hasSelection
    }

    private inline fun processRadioButtons(radioGroup: RadioGroup, action: (RadioButton) -> Unit): Boolean {
        for (i in 0 until radioGroup.childCount) {
            val child = radioGroup.getChildAt(i)
            if (child is LinearLayout) {
                for (j in 0 until child.childCount) {
                    val button = child.getChildAt(j)
                    if (button is RadioButton) action(button)
                }
            } else if (child is RadioButton) {
                action(child)
            }
        }
        return false
    }

    fun getSelectedRadioButton(radioGroup: RadioGroup): RadioButton? {
        for (i in 0 until radioGroup.childCount) {
            val child = radioGroup.getChildAt(i)
            if (child is LinearLayout) {
                for (j in 0 until child.childCount) {
                    val button = child.getChildAt(j)
                    if (button is RadioButton && button.isChecked) {
                        return button
                    }
                }
            } else if (child is RadioButton && child.isChecked) {
                return child
            }
        }
        return null
    }

}
