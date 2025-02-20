package com.example.hrr_android

import android.util.Log
import com.example.hrr_android.access.repository.AuthRepository
import com.example.hrr_android.challenge.model.ChallengeDetail
import com.example.hrr_android.challenge.model.WeeklyVerificationResponse
import retrofit2.Response
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
}