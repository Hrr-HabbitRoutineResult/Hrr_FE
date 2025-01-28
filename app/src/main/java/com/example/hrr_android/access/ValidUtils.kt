package com.example.hrr_android.access

import android.content.Context
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar

object ValidUtils {

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