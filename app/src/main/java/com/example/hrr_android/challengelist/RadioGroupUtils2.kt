package com.example.hrr_android.challengelist

import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment

object RadioGroupUtils {

    fun setupRadioGroup(radioGroup: RadioGroup, fragment: Fragment) {
        processRadioButtons(radioGroup) { button ->
            button.setOnClickListener {
                updateRadioGroupSelection(radioGroup, button)
            }
        }
    }

    private inline fun processRadioButtons(radioGroup: RadioGroup, action: (RadioButton) -> Unit) {
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
    }

    private fun updateRadioGroupSelection(radioGroup: RadioGroup, selectedButton: RadioButton) {
        processRadioButtons(radioGroup) { button ->
            button.isSelected = (button == selectedButton)
        }
    }
}
