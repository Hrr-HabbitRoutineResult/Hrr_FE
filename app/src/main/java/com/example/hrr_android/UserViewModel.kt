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

    private val _profile = MutableLiveData<UserResponse?>()     // 내부 접근용
    val profile: LiveData<UserResponse?> get() = _profile       // 외부 읽기용

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

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

    fun fetchChallengesOngoing() {
        Log.d("asdf", "fetchChallengesOngoing() 호출됨") // 디버깅용 로그
        viewModelScope.launch {
            val result = userRepository.getChallengesOngoing()
            Log.d("asdf", "API 응답: $result") // API 응답 데이터 확인
            _challengesOngoing.value = result
        }
    }
}
