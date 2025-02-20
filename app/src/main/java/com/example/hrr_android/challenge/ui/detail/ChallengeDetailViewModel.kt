package com.example.hrr_android.challenge.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrr_android.ChallengeRepository
import com.example.hrr_android.UserResponse
import com.example.hrr_android.challenge.model.ChallengeDetail
import com.example.hrr_android.challenge.model.WeeklyVerificationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeDetailViewModel @Inject constructor(
    private val repository: ChallengeRepository
) : ViewModel() {

    // 챌린지 상세 정보 조회
    private val _challengeState = MutableStateFlow<Result<ChallengeDetail>?>(null)
    val challengeState = _challengeState.asStateFlow()

    fun fetchChallengeDetail(challengeId: Int) {
        viewModelScope.launch {
            _challengeState.value = repository.getChallengeDetail(challengeId)
        }
    }

    // 사용자 프로필 정보 조회
    private val _userProfileState = MutableStateFlow<Result<UserResponse>?>(null)
    val userProfileState = _userProfileState.asStateFlow()

    fun fetchUserProfile(userId: Int) {
        viewModelScope.launch {
            _userProfileState.value = repository.getUserProfile(userId)
        }
    }

    // 챌린지 참가 API 응답 상태 관리
    private val _joinState = MutableStateFlow<Result<ChallengeDetail>?>(null)
    val joinState = _joinState.asStateFlow()

    fun joinChallenge(challengeId: Int) {
        viewModelScope.launch {
            _joinState.value = repository.joinChallenge(challengeId)
        }
    }

    // 주간 인증 상태 관리
    private val _weeklyVerificationState = MutableStateFlow<Result<WeeklyVerificationResponse>?>(null)
    val weeklyVerificationState = _weeklyVerificationState.asStateFlow()

    fun fetchWeeklyVerification(challengeId: Int) {
        viewModelScope.launch {
            _weeklyVerificationState.value = repository.getWeeklyVerification(challengeId)
        }
    }
}