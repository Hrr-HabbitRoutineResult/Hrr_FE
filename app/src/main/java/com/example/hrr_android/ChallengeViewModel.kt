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

}
