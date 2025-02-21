package com.example.hrr_android.challenge.certification.ui.photo

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrr_android.ChallengeRepository
import com.example.hrr_android.challenge.model.VerificationUploadResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PhotoCertificationViewModel @Inject constructor(
    private val repository: ChallengeRepository
) : ViewModel() {
    private val _photoUploadState = MutableLiveData<Result<String>>()
    val photoUploadState: LiveData<Result<String>> get() = _photoUploadState

    private val _verificationState = MutableLiveData<Result<VerificationUploadResponse>>()
    val verificationState: LiveData<Result<VerificationUploadResponse>> get() = _verificationState

    fun uploadPhoto(bitmap: Bitmap, context: Context) {
        viewModelScope.launch {
            try {
                // 📌 1️⃣ Bitmap을 File로 변환
                val file = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
                file.outputStream().use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    out.flush()
                }

                // 📌 2️⃣ File을 RequestBody로 변환
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

                // 📌 3️⃣ MultipartBody.Part로 변환
                val photoPart = MultipartBody.Part.createFormData("photo", file.name, requestFile)

                // 📌 4️⃣ 서버에 업로드 (Retrofit API 호출)
                _photoUploadState.value = repository.uploadPhoto(photoPart)
            } catch (e: Exception) {
                _photoUploadState.value = Result.failure(e)
            }
        }
    }


    fun uploadVerification(
        challengeId: Int,
        photoUrl: String,
        title: String,
        content: String,
        isQuestion: Boolean
    ) {
        viewModelScope.launch {
            _verificationState.value = repository.uploadVerification(
                challengeId = challengeId,
                photoUrl = photoUrl,
                title = title,
                content = content,
                isQuestion = isQuestion
            )
        }
    }

    fun resetStates() {
        _photoUploadState.value = null
        _verificationState.value = null
    }
}