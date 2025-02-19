package com.example.hrr_android.access.network

import com.example.hrr_android.ApiResponse
import com.example.hrr_android.access.model.EmailConfirmRequest
import com.example.hrr_android.access.model.EmailConfirmResponse
import com.example.hrr_android.access.model.EmailVerificationRequest
import com.example.hrr_android.access.model.EmailVerificationResponse
import com.example.hrr_android.access.model.KakaoLoginRequest
import com.example.hrr_android.access.model.KakaoLoginResponse
import com.example.hrr_android.access.model.LoginRequest
import com.example.hrr_android.access.model.LoginResponse
import com.example.hrr_android.access.model.NicknameCheckRequest
import com.example.hrr_android.access.model.NicknameCheckResponse
import com.example.hrr_android.access.model.RegisterRequest
import com.example.hrr_android.access.model.RegisterResponse
import com.example.hrr_android.access.model.TokenRequest
import com.example.hrr_android.access.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    // 로그인 API
    @POST("api/v1/auth/login/email")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    // 이메일 인증 코드 전송 API
    @POST("api/v1/auth/send-verify-email")
    suspend fun sendVerificationCode(
        @Body request: EmailVerificationRequest
    ): Response<EmailVerificationResponse>

    // 이메일 인증 코드 확인 API
    @POST("api/v1/auth/check-email-verification-code")
    suspend fun confirmVerificationCode(
        @Body request: EmailConfirmRequest
    ): Response<ApiResponse<EmailConfirmResponse>>

    // 닉네임 중복 확인 API
    @POST("api/v1/auth/check-nickname")
    suspend fun checkNickname(
        @Body request: NicknameCheckRequest
    ): Response<NicknameCheckResponse>

    // 회원가입 API
    @POST("api/v1/auth/register")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<ApiResponse<RegisterResponse>>

    // 카카오 로그인 API
    @POST("api/v1/auth/login/kakao")
    @Headers("Content-Type: application/json")
    suspend fun loginWithKakao(
        @Body request: KakaoLoginRequest
    ): Response<KakaoLoginResponse>

    // 토큰 갱신 API
    @POST("api/v1/auth/token")
    suspend fun refreshToken(
        @Body request: TokenRequest
    ): Response<ApiResponse<TokenResponse>>
}