package com.example.hrr_android.challenge.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrr_android.ChallengeRepository
import com.example.hrr_android.UserResponse
import com.example.hrr_android.challenge.model.ChallengeDetail
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

    // 사용자 프로필 정보 조회
    private val _userProfileState = MutableStateFlow<Result<UserResponse>?>(null)
    val userProfileState = _userProfileState.asStateFlow()

    fun fetchChallengeDetail(challengeId: Int) {
        viewModelScope.launch {
            _challengeState.value = repository.getChallengeDetail(challengeId)
        }
    }

    fun fetchUserProfile(userId: Int) {
        viewModelScope.launch {
            _userProfileState.value = repository.getUserProfile(userId)
        }
    }
}