package com.example.hrr_android

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.hrr_android.access.repository.AuthRepository
import com.example.hrr_android.challenge.model.ChallengeDetail
import com.example.hrr_android.challenge.model.PostResponse
import com.example.hrr_android.challenge.model.WeeklyVerificationResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.hrr_android.challenge.model.VerificationRequest
import com.example.hrr_android.challenge.model.VerificationUploadResponse
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChallengeRepository @Inject constructor(
    private val challengeService: ChallengeService,
    private val authRepository: AuthRepository
) {
    suspend fun getChallengeHotness(): Result<List<ChallengeHotness>> {
        return try {
            val response = challengeService.getChallengeHotness()

            if (response.isSuccessful) {
                val apiResponse = response.body()
                    ?: return Result.failure(Exception("서버 응답이 비어 있습니다."))
                return (apiResponse.success?.challenges?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("서버 응답이 올바르지 않습니다.")))
            } else {
                Result.failure(Exception("서버 오류 발생: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("네트워크 연결에 실패했습니다. 인터넷을 확인하세요."))
        } catch (e: Exception) {
            Result.failure(Exception("알 수 없는 오류 발생: ${e.localizedMessage}"))
        }
    }

    // 챌린지 상세 정보 조회 Repository 메서드
    suspend fun getChallengeDetail(challengeId: Int): Result<ChallengeDetail> {
        return try {
            val response = challengeService.getChallengeDetail(challengeId)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                    ?: return Result.failure(Exception("서버 응답이 비어 있습니다."))
                return (apiResponse.success?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("서버 응답이 올바르지 않습니다.")))
            } else {
                Result.failure(Exception("서버 오류 발생: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("네트워크 연결에 실패했습니다. 인터넷을 확인하세요."))
        } catch (e: Exception) {
            Result.failure(Exception("알 수 없는 오류 발생: ${e.localizedMessage}"))
        }
    }
    // 사용자 프로필 정보 조회 Repository 메서드
    suspend fun getUserProfile(userId: Int): Result<UserResponse> {
        return try {
            val response = challengeService.getUserProfile(userId)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                    ?: return Result.failure(Exception("서버 응답이 비어 있습니다."))
                return (apiResponse.success?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("서버 응답이 올바르지 않습니다.")))
            } else {
                Result.failure(Exception("서버 오류 발생: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("네트워크 연결에 실패했습니다. 인터넷을 확인하세요."))
        } catch (e: Exception) {
            Result.failure(Exception("알 수 없는 오류 발생: ${e.localizedMessage}"))
        }
    }
    // 특정 챌린지 참가 Repository 메서드
    suspend fun joinChallenge(challengeId: Int): Result<ChallengeDetail> {
        return try {
            val response = challengeService.joinChallenge(challengeId)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                    ?: return Result.failure(Exception("서버 응답이 비어 있습니다."))
                return (apiResponse.success?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("서버 응답이 올바르지 않습니다.")))
            } else {
                // 에러 응답 처리
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody?.contains("already joined") == true) {
                    "이미 참여 중인 챌린지입니다."
                } else {
                    "챌린지 참가에 실패했습니다."
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: IOException) {
            Result.failure(Exception("네트워크 연결에 실패했습니다. 인터넷을 확인하세요."))
        } catch (e: Exception) {
            Result.failure(Exception("알 수 없는 오류 발생: ${e.localizedMessage}"))
        }
    }
    // 이번 주 인증 현황 Repository 메서드
    suspend fun getWeeklyVerification(challengeId: Int): Result<WeeklyVerificationResponse> {
        return try {
            val response = challengeService.getWeeklyVerification(challengeId)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                    ?: return Result.failure(Exception("서버 응답이 비어 있습니다."))
                if (apiResponse.success != null) {
                    Result.success(apiResponse.success)
                } else {
                    Result.failure(Exception("서버 응답이 올바르지 않습니다."))
                }
            } else {
                Result.failure(Exception("서버 오류 발생: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("네트워크 연결에 실패했습니다. 인터넷을 확인하세요."))
        } catch (e: Exception) {
            Result.failure(Exception("알 수 없는 오류 발생: ${e.localizedMessage}"))
        }
    }

    // 이미지 업로드
    suspend fun uploadPhoto(imageUri: Uri, context: Context): Result<String> {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val bytes = inputStream?.readBytes() ?: return Result.failure(Exception("이미지를 읽을 수 없습니다."))

            val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData(
                "image",
                "image.jpg",
                requestBody
            )

            val response = challengeService.uploadPhoto(part)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                    ?: return Result.failure(Exception("서버 응답이 비어 있습니다."))
                return (apiResponse.success?.photoUrl?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("서버 응답이 올바르지 않습니다.")))
            } else {
                Result.failure(Exception("서버 오류 발생: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("네트워크 연결에 실패했습니다. 인터넷을 확인하세요."))
        } catch (e: Exception) {
            Result.failure(Exception("알 수 없는 오류 발생: ${e.localizedMessage}"))
        }
    }
    // 인증 업로드
    suspend fun uploadVerification(
        challengeId: Int,
        photoUrl: String,
        title: String,
        content: String,
        isQuestion: Boolean
    ): Result<VerificationUploadResponse> {
        return try {
            val request = VerificationRequest(
                photoUrl = photoUrl,
                title = title,
                content = content,
                textUrl = null,
                question = if (isQuestion) 1 else 0
            )

            val response = challengeService.uploadVerification(challengeId, request)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                    ?: return Result.failure(Exception("서버 응답이 비어 있습니다."))
                return (apiResponse.success?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("서버 응답이 올바르지 않습니다.")))
            } else {
                Result.failure(Exception("서버 오류 발생: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("네트워크 연결에 실패했습니다. 인터넷을 확인하세요."))
        } catch (e: Exception) {
            Result.failure(Exception("알 수 없는 오류 발생: ${e.localizedMessage}"))
        }
    }

    // 인증 상세 조회
    suspend fun getVerificationDetail(verificationId: Int): Result<PostResponse> {
        return try {
            val response = challengeService.getVerificationDetail(verificationId)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                    ?: return Result.failure(Exception("서버 응답이 비어 있습니다."))
                return (apiResponse.success?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("서버 응답이 올바르지 않습니다.")))
            } else {
                Result.failure(Exception("서버 오류 발생: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("네트워크 연결에 실패했습니다. 인터넷을 확인하세요."))
        } catch (e: Exception) {
            Result.failure(Exception("알 수 없는 오류 발생: ${e.localizedMessage}"))
        }
    }

}