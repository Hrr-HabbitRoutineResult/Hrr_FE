package com.example.hrr_android.access

import androidx.lifecycle.LiveData

object AuthEventManager {
    private val _logoutEvent = SingleLiveEvent<Unit>()
    val logoutEvent: LiveData<Unit>
        get() = _logoutEvent

    fun postLogoutEvent() {
        _logoutEvent.call()
    }
}
