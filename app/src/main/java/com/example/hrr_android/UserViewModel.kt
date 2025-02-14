package com.example.hrr_android

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String?>()      // 내부 접근용
    val errorMessage: LiveData<String?> get() = _errorMessage   // 외부 읽기용

    // 사용자 정보 조회
    private val _profile = MutableLiveData<UserResponse?>()
    val profile: LiveData<UserResponse?> get() = _profile

    // 참가 중인 챌린지 조회
    private val _challengesOngoing = MutableLiveData<Result<List<ChallengesOngoing>>>()
    val challengesOngoing: LiveData<Result<List<ChallengesOngoing>>> get() = _challengesOngoing

    fun loadProfile() {
        viewModelScope.launch {
            val result = userRepository.loadProfile()
            result.onSuccess{
                _profile.postValue(result.getOrNull()) // 성공 시 데이터 업데이트
            }.onFailure {
                _errorMessage.postValue(result.exceptionOrNull()?.message) // 실패 시 에러 메시지 전달
            }
        }
    }

    // 챌린지 기록 조회
    private val _history = MutableLiveData<List<HistoryResponse>?>()
    val history: LiveData<List<HistoryResponse>?> get() = _history

    fun getChallengeHistory() {
        viewModelScope.launch {
            val result = userRepository.getChallengeHistory()
            result.onSuccess{
                _history.postValue(result.getOrNull()) // 성공 시 데이터 업데이트
            }.onFailure {
                _errorMessage.postValue(result.exceptionOrNull()?.message) // 실패 시 에러 메시지 전달
            }
        }
    }

    // 참가 중인 챌린지 조회
    fun fetchChallengesOngoing() {
        viewModelScope.launch {
            val result = userRepository.getChallengesOngoing()
            _challengesOngoing.value = result

            result.onFailure { error ->
                _errorMessage.value = error.localizedMessage ?: "Unknown error"
            }
        }
    }
}
