package com.example.hrr_android.access

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.hrr_android.R
import com.google.android.material.snackbar.Snackbar

object ValidUtils {

    fun getTextColorDefault(context: Context): Int {
        return ContextCompat.getColor(context, R.color.text_tertiary)
    }

    fun getTextColorError(context: Context): Int {
        return ContextCompat.getColor(context, R.color.sub_01)
    }

    fun getBackgroundDefault(context: Context): Drawable? {
        return ContextCompat.getDrawable(context, R.drawable.bg_input_field)
    }

    fun getBackgroundError(context: Context): Drawable? {
        return ContextCompat.getDrawable(context, R.drawable.bg_input_field_error)
    }

    fun getButtonActiveBackground(context: Context): Drawable? {
        return ContextCompat.getDrawable(context, R.drawable.btn_orange_white_30)
    }

    fun getButtonInactiveBackground(context: Context): Drawable? {
        return ContextCompat.getDrawable(context, R.drawable.btn_grey_30)
    }

    // 이메일 유효성 검사 함수
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // 비밀번호 유효성 검사 함수
    fun isValidPassword(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,20}")
        return passwordRegex.containsMatchIn(password)
    }

    // 닉네임 유효성 검사 함수
    fun isValidNickname(nickname: String): Boolean {
        return Regex("^[가-힣a-zA-Z0-9]{1,10}").matches(nickname)
    }

    fun updateButtonState(
        frameLayout: FrameLayout,
        textView: TextView,
        imageView: ImageView,
        isEnabled: Boolean
    ) {
        // FrameLayout 활성화/비활성화
        frameLayout.isEnabled = isEnabled

        // TextView와 ImageView의 상태가 isEnabled에 따라 drawable 자동 적용
        textView.isEnabled = isEnabled
        imageView.isEnabled = isEnabled
    }


    // 키보드 숨기기 함수
    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showSnackbar(view: View, message: String, anchorView: View? = null) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snackbar.anchorView = anchorView  // 특정 버튼 위에 고정
        snackbar.show()
    }
}