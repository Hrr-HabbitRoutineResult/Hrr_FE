package com.example.hrr_android.access.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    val message: String,
    @SerializedName("created_user")
    val createdUser: CreatedUser
) {
    data class CreatedUser(
        val id: Int,
        val email: String,
        val nickname: String
    )
}
