package com.example.hrr_android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrr_android.access.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherUserViewModel@Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // 에러
    private val _errorMessage = MutableLiveData<String?>()      // 내부 접근용
    val errorMessage: LiveData<String?> get() = _errorMessage   // 외부 읽기용

    // 사용자 정보 조회
    private val _profile = MutableLiveData<UserResponse?>()
    val profile: LiveData<UserResponse?> get() = _profile

    fun loadProfile(userId: Int) {
        viewModelScope.launch {
            val result = userRepository.loadProfile(userId)
            result.onSuccess{
                _profile.postValue(result.getOrNull()) // 성공 시 데이터 업데이트
            }.onFailure {
                _errorMessage.postValue(result.exceptionOrNull()?.message) // 실패 시 에러 메시지 전달
            }
        }
    }

}