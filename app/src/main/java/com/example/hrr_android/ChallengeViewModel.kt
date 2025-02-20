package com.example.hrr_android

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrr_android.makechallenge.MakeChallengeRequest
import com.example.hrr_android.makechallenge.MakeChallengeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String?>()      // 내부 접근용
    val errorMessage: LiveData<String?> get() = _errorMessage   // 외부 읽기용

    // 인기 챌린지 조회
    private val _challengeHotness = MutableLiveData<Result<List<ChallengeHotness>>?>()
    val challengesHotness: LiveData<Result<List<ChallengeHotness>>?> get() = _challengeHotness

    fun fetchChallengesHotness() {
        viewModelScope.launch {
            val result = challengeRepository.getChallengeHotness()
            _challengeHotness.value = result

            result.onFailure { error ->
                _errorMessage.value = error.localizedMessage ?: "Unknown error"
            }
        }
    }

    // 챌린지 개설
    private val _makeChallengeResult = MutableLiveData<Result<MakeChallengeResponse>?>()
    val makeChallengeResult: LiveData<Result<MakeChallengeResponse>?> get() = _makeChallengeResult

    fun makeChallenge(request: MakeChallengeRequest) {
        viewModelScope.launch {
            val result = challengeRepository.makeChallenge(request)
            _makeChallengeResult.value = result

            result.onFailure { error ->
                _errorMessage.value = error.localizedMessage ?: "챌린지 생성 중 오류 발생"
            }
        }
    }
}
