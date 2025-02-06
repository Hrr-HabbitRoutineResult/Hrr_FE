package com.example.hrr_android

import android.content.Context
import com.example.hrr_android.access.repository.AuthRepository

object AuthRepositoryProvider {
    lateinit var authRepository: AuthRepository
        private set

    fun initialize(context: Context) {
        authRepository = AuthRepository(context)
    }
}