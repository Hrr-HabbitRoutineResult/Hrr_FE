package com.example.hrr_android.access.network

import com.example.hrr_android.access.model.LoginRequest
import com.example.hrr_android.access.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/v1/auth/login/email")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}