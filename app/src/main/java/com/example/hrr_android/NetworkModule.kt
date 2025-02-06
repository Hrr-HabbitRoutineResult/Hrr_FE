package com.example.hrr_android

import android.util.Log
import com.example.hrr_android.access.network.AuthService
import com.example.hrr_android.access.repository.AuthRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import java.util.concurrent.TimeUnit
import javax.inject.Provider

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    //private const val BASE_URL = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(authRepositoryProvider: Provider<AuthRepository>): OkHttpClient {
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

        val authInterceptor = AuthInterceptor(authRepositoryProvider)

        return OkHttpClient.Builder()
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
        Log.d("BaseUrlCheck", "BASE_URL: ${BuildConfig.BASE_URL}")
        return try{
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }catch (e: Exception) {
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
}
