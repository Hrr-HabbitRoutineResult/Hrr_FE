package com.example.hrr_android

import android.content.Context
import android.util.Log
import com.example.hrr_android.access.repository.AuthRepository
import com.example.hrr_android.makechallenge.MakeChallengeRequest
import com.example.hrr_android.makechallenge.MakeChallengeResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChallengeRepository @Inject constructor(
    @ApplicationContext private val context: Context,
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

    suspend fun makeChallenge(request: MakeChallengeRequest): Result<MakeChallengeResponse> {
        return try {
            val response = challengeService.makeChallenge(request)

            if (response.isSuccessful) {
                val apiResponse = response.body()
                    ?: return Result.failure(Exception("서버 응답이 비어 있습니다."))

                if (apiResponse.resultType == "FAIL") {
                    return Result.failure(Exception("서버 오류 발생: ${apiResponse.error}"))
                }

                apiResponse.success?.let {
                    val challengeId = it.result.challenge.id
                    saveChallengeId(challengeId)

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

    fun saveChallengeId(challengeId: Int) {
        val sharedPreferences =
            context.getSharedPreferences("challenge_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("CHALLENGE_ID", challengeId).apply()
        Log.d("ChallengeRepository", "🟢 challengeId ($challengeId) 저장 완료")
    }
}
