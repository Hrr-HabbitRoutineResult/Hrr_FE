package com.example.hrr_android.challenge.certification.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrr_android.ChallengeRepository
import com.example.hrr_android.challenge.model.PostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: ChallengeRepository
) : ViewModel() {
    private val _postState = MutableStateFlow<Result<PostResponse>?>(null)
    val postState = _postState.asStateFlow()

    fun fetchPostDetail(verificationId: Int) {
        viewModelScope.launch {
            _postState.value = repository.getVerificationDetail(verificationId)
        }
    }
}