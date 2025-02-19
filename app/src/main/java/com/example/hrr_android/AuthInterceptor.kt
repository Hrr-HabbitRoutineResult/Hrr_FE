package com.example.hrr_android

import android.util.Log
import com.example.hrr_android.access.AuthEventManager
import com.example.hrr_android.access.TokenManager
import com.example.hrr_android.access.repository.AuthRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.runBlocking

class AuthInterceptor @Inject constructor(
    private val authRepositoryProvider: Provider<AuthRepository>,
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // Authorization이 필요 없는 api 경로들
        val excludedPatterns = listOf(  // 공통 경로로 묶을 수 있는 경우
            // Regex("^/api/v1/auth/.*"),   // 로그인 관련
            Regex("^/api/v1/posts/hotness$"),      // 전체 인기글
            Regex("^/api/v1/posts$"),              // 게시판 리스트 불러오기
            Regex("^/api/v1/posts/\\d+$"),         // 게시글 불러오기
            Regex("^/api/v1/board/\\d+/hotness$")  // 게시판 내 HOT 게시글
        )
        // Todo: 일단 쓰는 경로 중에서만 적어놨으니 계속 추가하기
        val excludedEndpoints = setOf(  // 그 외 개별 경로
            "/api/v1/users/{userId}",
            "/api/v1/auth/login/email",
            "/api/v1/auth/send-verify-email",
            "/api/v1/auth/check-email-verification-code",
            "/api/v1/auth/check-nickname",
            "/api/v1/auth/register",
            "/api/v1/auth/login/kakao",
            "/api/v1/auth/token"
        )

        // 권한 헤더가 필요 없는 api 경로를 제외하고 자동으로 헤더 추가
        val isExcluded = excludedEndpoints.contains(originalRequest.url.encodedPath) ||
                excludedPatterns.any { it.matches(originalRequest.url.encodedPath) }

        //val isExcluded = excludedPatterns.any { it.matches(originalRequest.url.encodedPath) }


        if (!isExcluded && originalRequest.header("Authorization") == null) {
            val token = tokenManager.getAccessToken()

            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
                Log.d("AuthInterceptor", "Authorization 헤더: ${requestBuilder.build().header("Authorization")}")

            }
        }

        val modifiedRequest = requestBuilder.build()

        // 최종 요청 로그 추가 (확인용)
        Log.d("AuthInterceptor", "Final Request Headers: ${modifiedRequest.headers}")

        // 요청 바디 로그 (확인용, GET 요청이면 없음)
        modifiedRequest.body?.let { body ->
            val buffer = okio.Buffer()
            body.writeTo(buffer)
            Log.d("AuthInterceptor", "Final Request Body: ${buffer.readUtf8()}")
        } ?: Log.e("AuthInterceptor", "요청 바디가 없습니다. (GET 요청이거나, 바디가 null일 가능성 있음)")

        var response = chain.proceed(modifiedRequest)

        if (isAccessTokenExpired(response)) {
            Log.d("AuthInterceptor", "액세스 토큰 만료 감지, 새 액세스 토큰 요청 시도")

            val newAccessToken = runBlocking {
                val authRepository = authRepositoryProvider.get()
                authRepository.refreshAccessToken()
            }

            if (!newAccessToken.isNullOrEmpty()) {
                tokenManager.saveAccessToken(newAccessToken)
                val newRequest = modifiedRequest.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
                response = chain.proceed(newRequest)
            } else {
                Log.e("AuthInterceptor", "리프레시 토큰이 유효하지 않음 - 강제 로그아웃 진행")
                tokenManager.clearTokens()
                AuthEventManager.postLogoutEvent()
            }
        } else if (isRefreshTokenInvalid(response)) {
            Log.e("AuthInterceptor", "리프레시 토큰 만료 감지 - 강제 로그아웃 진행")
            tokenManager.clearTokens()
            AuthEventManager.postLogoutEvent()
        }

        return response
    }

    // 서버 응답에서 액세스 토큰 만료 여부 감지
    private fun isAccessTokenExpired(response: Response): Boolean {
        val responseBodyString = response.peekBody(Long.MAX_VALUE).string()
        Log.d("AuthInterceptor", "서버 응답 상태 코드: ${response.code}")
        Log.d("AuthInterceptor", "서버 응답 바디: $responseBodyString")

        return response.code == 401
    }

    // 서버 응답에서 리프레시 토큰 만료 여부 감지
    private fun isRefreshTokenInvalid(response: Response): Boolean {
        val responseBodyString = response.peekBody(Long.MAX_VALUE).string()
        Log.d("AuthInterceptor", "서버 응답 상태 코드: ${response.code}")
        Log.d("AuthInterceptor", "서버 응답 바디: $responseBodyString")

        return response.code == 403
    }
}
