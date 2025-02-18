package com.example.hrr_android

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrr_android.access.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
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
            val result = userRepository.loadProfile(authRepository.getUserId())
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
            val result = userRepository.getChallengesOngoing(authRepository.getUserId())
            _challengesOngoing.value = result

            result.onFailure { error ->
                _errorMessage.value = error.localizedMessage ?: "Unknown error"
            }
        }
    }

    // 최근 완주한 챌린지 조회
    private val _challengesEnd = MutableLiveData<ChallengeEndResponse?>()
    val challengesEnd: LiveData<ChallengeEndResponse?> get() = _challengesEnd

    fun loadChallengesEnd(){
        viewModelScope.launch {
            val result = userRepository.getChallengesEnd(authRepository.getUserId())
            result.onSuccess{
                _challengesEnd.postValue(result.getOrNull()) // 성공 시 데이터 업데이트
            }.onFailure {
                _errorMessage.postValue(result.exceptionOrNull()?.message) // 실패 시 에러 메시지 전달
            }
        }
    }

    // 팔로워 리스트 조회
    private val _followers = MutableLiveData<FollowResponse?>()
    val followers: LiveData<FollowResponse?> get() = _followers

    fun loadFollowers(){
        viewModelScope.launch {
            val result = userRepository.getFollowers(authRepository.getUserId())
            result.onSuccess{
                _followers.postValue(result.getOrNull()) // 성공 시 데이터 업데이트
            }.onFailure {
                _errorMessage.postValue(result.exceptionOrNull()?.message) // 실패 시 에러 메시지 전달
            }
        }
    }

    // 팔로잉 리스트 조회
    private val _followings = MutableLiveData<FollowResponse?>()
    val followings: LiveData<FollowResponse?> get() = _followings

    fun loadFollowings(){
        viewModelScope.launch {
            val result = userRepository.getFollowings(authRepository.getUserId())
            result.onSuccess{
                _followings.postValue(result.getOrNull()) // 성공 시 데이터 업데이트
            }.onFailure {
                _errorMessage.postValue(result.exceptionOrNull()?.message) // 실패 시 에러 메시지 전달
            }
        }
    }

    // 팔로우 실행 후 팔로워/팔로잉 리스트 업데이트
    fun follow(userId: Int) {
        viewModelScope.launch {
            val result = userRepository.follow(userId)
            result.onSuccess {
                loadFollowers()  // 팔로워 리스트 갱신
                loadFollowings() // 팔로잉 리스트 갱신
            }.onFailure {
                _errorMessage.postValue(result.exceptionOrNull()?.message) // 에러 메시지 전달
            }
        }
    }

    // 언팔로우 실행 후 팔로워/팔로잉 리스트 업데이트
    fun unfollow(userId: Int) {
        viewModelScope.launch {
            val result = userRepository.unfollow(userId)
            result.onSuccess {
                loadFollowers()  // 팔로워 리스트 갱신
                loadFollowings() // 팔로잉 리스트 갱신
            }.onFailure {
                _errorMessage.postValue(result.exceptionOrNull()?.message) // 에러 메시지 전달
            }
        }
    }

    // 특정 사용자 차단
    fun blockUser(blockUserId: Int){
        viewModelScope.launch {
            val result = userRepository.blockUser(blockUserId)
            result.onSuccess {

            }.onFailure {
                _errorMessage.postValue(result.exceptionOrNull()?.message) // 에러 메시지 전달
            }
        }
    }

    // 특정 사용자 차단 해제
    fun unblockUser(blockedUserId: Int){
        viewModelScope.launch {
            val result = userRepository.unblockUser(blockedUserId)
            result.onSuccess {

            }.onFailure {
                _errorMessage.postValue(result.exceptionOrNull()?.message) // 에러 메시지 전달
            }
        }
    }

    /*
    * 사용자 뱃지 목록
    * */
    private val _badges = MutableLiveData<BadgeResponse?>()
    val badges: LiveData<BadgeResponse?> get() = _badges

    fun loadBadges(){
        viewModelScope.launch {
            val result = userRepository.getMyBadges(authRepository.getUserId())
            result.onSuccess{
                _badges.postValue(result.getOrNull()) // 성공 시 데이터 업데이트
            }.onFailure {
                _errorMessage.postValue(result.exceptionOrNull()?.message) // 실패 시 에러 메시지 전달
            }
        }
    }

    /*
    * 사용자 정보 수정
    * */

    fun updateProfile(profileUpdateRequest: ProfileUpdateRequest){
        viewModelScope.launch {
            val result = userRepository.updateProfile(profileUpdateRequest)
            result.onSuccess{

            }.onFailure {
                _errorMessage.postValue(result.exceptionOrNull()?.message) // 실패 시 에러 메시지 전달
            }
        }
    }

}
