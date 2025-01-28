package com.example.hrr_android.access

enum class NavigatePasswordFragment(val fragmentName: String, val title: String) {
    VERIFICATION("VerificationFragment", "비밀번호 찾기"),
    RESET("PasswordResetFragment", "비밀번호 재설정");

    companion object {
        // 문자열 값으로 Enum 객체를 반환하는 함수
        fun fromFragmentName(name: String?): NavigatePasswordFragment {
            return values().find { it.fragmentName == name } ?: RESET
        }
    }
}