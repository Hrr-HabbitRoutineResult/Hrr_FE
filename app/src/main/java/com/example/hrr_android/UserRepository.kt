package com.example.hrr_android

import android.util.Log
import com.example.hrr_android.access.repository.AuthRepository
import com.example.hrr_android.onboarding.model.OnboardingRequest
import com.example.hrr_android.onboarding.model.OnboardingResponse
import com.example.hrr_android.onboarding.model.OnboardingSuccess
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userService: UserService,
    private val authRepository: AuthRepository
) {
    // api 응답 처리 공통 로직
    private suspend fun <T> handleResponse(
        responseAll: suspend () -> Response<ApiResponse<T>>
    ): Result<T> {
        return try {
            val response = responseAll()

            if (response.isSuccessful) {
                val responseBody = response.body() ?: return Result.failure(Exception("서버 응답이 비어 있습니다."))

                val data = responseBody.success

                return if (data != null) {
                    Result.success(data)
                } else {
                    Result.failure(Exception("서버 응답이 올바르지 않습니다."))
                }
            } else {
                Result.failure(Exception("서버 오류 발생: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("네트워크 연결에 실패했습니다. 인터넷을 확인하세요."))
        } catch (e: Exception) {
            Result.failure(Exception("알 수 없는 오류가 발생했습니다: ${e.localizedMessage}"))
        }
    }

    // 사용자 정보 조회
    suspend fun loadProfile(userId: Int): Result<UserResponse>{
        return handleResponse { userService.getUserInfo(userId) } // 함수 자체를 전달하여 호출 즉시 실행 방지,
    }

    // 챌린지 기록 조회
    suspend fun getChallengeHistory(): Result<List<HistoryResponse>>{
        return  handleResponse { userService.getChallengeHistory() }
    }

    // 진행중인 챌린지 조회
    suspend fun getChallengesOngoing(userId: Int): Result<List<ChallengesOngoing>> {
        return try {
            val response = userService.getChallengesOngoing(userId)

            if (response.isSuccessful) {
                val apiResponse = response.body()
                    ?: return Result.failure(Exception("서버 응답이 비어 있습니다."))
                return apiResponse.success?.ongoingChallenges?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("서버 응답이 올바르지 않습니다."))
            } else {
                Result.failure(Exception("서버 오류 발생: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("네트워크 연결에 실패했습니다. 인터넷을 확인하세요."))
        } catch (e: Exception) {
            Result.failure(Exception("알 수 없는 오류 발생: ${e.localizedMessage}"))
        }
    }

    // 사용자 맞춤 추천 챌린지 조회
    suspend fun getOnboardingChallenge(category: String): Result<List<OnboardingSuccess>> {
        return try {
            val requestBody = OnboardingRequest(category)
            val response = userService.getOnboardingChallenge(requestBody)

            if (response.isSuccessful) {
                val apiResponse: OnboardingResponse<OnboardingSuccess> = response.body()
                    ?: return Result.failure(Exception("서버 응답이 비어 있습니다."))

                return apiResponse.success?.let { list ->
                    Result.success(list)  // List<OnboardingSuccess> 반환
                } ?: Result.failure(Exception("서버 응답이 올바르지 않습니다."))
            } else {
                Result.failure(Exception("서버 오류 발생: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("네트워크 연결에 실패했습니다. 인터넷을 확인하세요."))
        } catch (e: Exception) {
            Result.failure(Exception("알 수 없는 오류 발생: ${e.localizedMessage}"))
        }
    }

    // 최근 완주한 챌린지 조회
    suspend fun getChallengesEnd(userId: Int): Result<ChallengeEndResponse>{
        return handleResponse { userService.getChallengesEnd(userId) }
    }

    // 팔로워 리스트 조회
    suspend fun getFollowers(userId: Int): Result<FollowResponse>{
        return handleResponse { userService.getFollowers(userId) }
    }

    // 팔로잉 리스트 조회
    suspend fun getFollowings(userId: Int): Result<FollowResponse>{
        return handleResponse { userService.getFollowings(userId) }
    }

    // 사용자 팔로우 처리
    suspend fun follow(followedUserId: Int): Result<FollowResult>{
        return handleResponse { userService.follow(followedUserId) }
    }

    // 사용자 언팔로우 처리
    suspend fun unfollow(unfollowedUserId: Int): Result<FollowResult>{
        return handleResponse { userService.unFollow(unfollowedUserId) }
    }

    // 사용자 차단 처리
    suspend fun blockUser(blockUserId: Int): Result<BlockResponse>{
        return handleResponse { userService.blockUser(blockUserId) }
    }

    // 사용자 차단 해제 처리
    suspend fun unblockUser(blockedUserId: Int): Result<BlockResponse>{
        return handleResponse { userService.unblockUser(blockedUserId) }
    }


}