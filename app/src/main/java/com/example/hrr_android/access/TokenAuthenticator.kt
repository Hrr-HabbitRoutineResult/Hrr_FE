package com.example.hrr_android.access

import android.util.Log
import com.example.hrr_android.access.network.AuthService
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.runBlocking

class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val authServiceProvider: Provider<AuthService>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // 이미 동일한 토큰으로 재시도한 경우 무한 루프 방지
        val currentToken = tokenManager.getAccessToken()
        if (response.request.header("Authorization")?.contains(currentToken ?: "") == true) {
            return null
        }
        val refreshToken = tokenManager.getRefreshToken() ?: return null

        Log.d("TokenAuthenticator", "Token expired, attempting to refresh token...")

        val newAccessToken = runBlocking {
            try {
                val authService = authServiceProvider.get()
                val result = authService.refreshToken(refreshToken)
                if (result.isSuccessful) {
                    result.body()?.accessToken.also {
                        Log.d("TokenAuthenticator", "Token refresh successful. New token: $it")
                    }
                } else {
                    Log.e("TokenAuthenticator", "Token refresh failed: ${result.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("TokenAuthenticator", "Exception during token refresh: ${e.message}")
                null
            }
        }

        return if (!newAccessToken.isNullOrEmpty()) {
            tokenManager.saveAccessToken(newAccessToken)
            response.request.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .build()
        } else {
            // 토큰 갱신 실패 시 저장된 토큰 삭제
            tokenManager.clearTokens()
            null
        }
    }
}
