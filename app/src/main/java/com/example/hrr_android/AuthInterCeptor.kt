package com.example.hrr_android

import android.util.Log
import com.example.hrr_android.access.repository.AuthRepository
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
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
            Regex("^/api/v1/auth/.*"),   // 로그인 관련
            Regex("^/api/v1/posts/hotness$"),      // 전체 인기글
            Regex("^/api/v1/posts$"),              // 게시판 리스트 불러오기
            Regex("^/api/v1/posts/\\d+$"),         // 게시글 불러오기
            Regex("^/api/v1/board/\\d+/hotness$")  // 게시판 내 HOT 게시글
        )
        // Todo: 나중에 개별 경로 추가되면 여기에 추가해서 아래 코드 사용
        /*val excludedEndpoints = setOf(  // 그 외 개별 경로

        )

        // 권한 헤더가 필요 없는 api 경로를 제외하고 자동으로 헤더 추가
        val isExcluded = excludedEndpoints.contains(originalRequest.url.encodedPath) ||
                excludedPatterns.any { it.matches(originalRequest.url.encodedPath) }*/

        val isExcluded = excludedPatterns.any { it.matches(originalRequest.url.encodedPath) }


        if (!isExcluded && originalRequest.header("Authorization") == null) {
            val token = authRepository.getAccessToken()

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

        val response = chain.proceed(modifiedRequest)

        val responseBody = response.body ?: return response

        val responseBodyString = responseBody.string()

        // 응답 내역 로그 출력 (확인용)
        Log.d("AuthInterceptor", "✅ Response Body: $responseBodyString")

        // 응답 내역 복구
        return response.newBuilder()
            .body(responseBodyString.toResponseBody(responseBody.contentType()))
            .build()


    }
}