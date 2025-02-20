package com.example.hrr_android

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

object BackPressUtils {
    fun handleBackPress(fragment: Fragment, action: (() -> Unit)? = null) {
        fragment.requireActivity().onBackPressedDispatcher.addCallback(
            fragment.viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    action?.invoke() ?: fragment.findNavController().navigateUp()
                }
            }
        )
    }
}