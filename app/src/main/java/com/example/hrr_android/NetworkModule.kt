package com.example.hrr_android

import android.util.Log
import com.example.hrr_android.access.TokenManager
import com.example.hrr_android.access.network.AuthService
import com.example.hrr_android.access.repository.AuthRepository
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.inject.Provider

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authRepositoryProvider: Provider<AuthRepository>,
        tokenManager: TokenManager
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            var filteredMessage = message
            val sensitiveKeys = listOf("password", "token", "auth")
            sensitiveKeys.forEach { key ->
                val regex = Regex("\"$key\"\\s*:\\s*\"[^\"]*\"")
                filteredMessage = filteredMessage.replace(regex, "\"$key\": \"*****\"")
            }
            Log.d("OkHttp", filteredMessage)
        }.apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }

        val authInterceptor = AuthInterceptor(authRepositoryProvider, tokenManager)

        return OkHttpClient.Builder()
            .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES)) // 연결 재사용
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setLenient() // 유연한 파싱 허용
            .create()
        return try {
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
        } catch (e: Exception) {
            Log.e("NetworkModule", "Retrofit 생성 중 오류 발생: ${e.message}")
            throw RuntimeException("Retrofit 초기화 실패")
        }
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideChallengeService(retrofit: Retrofit): ChallengeService {
        return retrofit.create(ChallengeService::class.java)
    }
}
