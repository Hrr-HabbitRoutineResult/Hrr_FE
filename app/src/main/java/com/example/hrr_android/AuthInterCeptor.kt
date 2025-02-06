package com.example.hrr_android

import com.example.hrr_android.access.repository.AuthRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider

class AuthInterceptor @Inject constructor(
    private val authRepositoryProvider: Provider<AuthRepository>
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
        val authRepository = authRepositoryProvider.get()

        // Authorization이 필요 없는 api 경로들
        val excludedPatterns = listOf(  // 공통 경로로 묶을 수 있는 경우
            Regex("^/api/v1/auth/.*")   // 로그인 관련
        )
        val excludedEndpoints = setOf(  // 그 외 개별 경로
            "/api/v1/posts/hotness",
            "/api/v1/posts",
            "/api/v1/posts/{postId}",
            "/api/v1/board/{boardId}/hotness"
        )

        // 권한 헤더가 필요 없는 api 경로를 제외하고 자동으로 헤더 추가
        val isExcluded = excludedEndpoints.contains(originalRequest.url.encodedPath) ||
                excludedPatterns.any { it.matches(originalRequest.url.encodedPath) }

        if (!isExcluded && originalRequest.header("Authorization") == null) {
            val token = authRepository.getAccessToken()
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }


        return chain.proceed(requestBuilder.build())
    }
}