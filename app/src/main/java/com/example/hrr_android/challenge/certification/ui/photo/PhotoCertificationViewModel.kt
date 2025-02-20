package com.example.hrr_android.challenge.certification.ui.photo

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrr_android.ChallengeRepository
import com.example.hrr_android.challenge.model.VerificationUploadResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PhotoCertificationViewModel @Inject constructor(
    private val repository: ChallengeRepository
) : ViewModel() {
    private val _photoUploadState = MutableStateFlow<Result<String>?>(null)
    val photoUploadState = _photoUploadState.asStateFlow()

    private val _verificationState = MutableStateFlow<Result<VerificationUploadResponse>?>(null)
    val verificationState = _verificationState.asStateFlow()

    fun uploadPhoto(bitmap: Bitmap, context: Context) {
        viewModelScope.launch {
            try {
                // 비트맵을 파일로 변환
                val file = File(context.cacheDir, "photo.jpg")
                file.outputStream().use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                }

                _photoUploadState.value = repository.uploadPhoto(Uri.fromFile(file), context)
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