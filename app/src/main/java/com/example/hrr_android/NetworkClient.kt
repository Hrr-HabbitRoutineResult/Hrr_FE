package com.example.hrr_android

import android.util.Log
import com.example.hrr_android.access.network.AuthService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkClient {
    private val BASE_URL = BuildConfig.BASE_URL

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        var filteredMessage = message
        val sensitiveKeys = listOf("password", "token", "auth") // 마스킹할 키 리스트

        sensitiveKeys.forEach { key ->
            val regex = Regex("\"$key\"\\s*:\\s*\"[^\"]*\"") // JSON 구조에서도 탐색 가능
            filteredMessage = filteredMessage.replace(regex, "\"$key\": \"*****\"")
        }

        Log.d("OkHttp", filteredMessage)
    }.apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }


    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    // API 서비스 생성 함수
    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }
    // 개별 API 서비스 제공
    val authService: AuthService by lazy { createService(AuthService::class.java) }
    val userService: UserService by lazy { createService(UserService::class.java) }
}