package com.example.hrr_android

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/v1/auth/login/email")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}