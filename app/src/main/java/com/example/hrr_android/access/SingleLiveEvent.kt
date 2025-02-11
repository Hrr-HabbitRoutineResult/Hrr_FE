package com.example.hrr_android.access

import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        // 내부 Observer를 감싸서 pending 상태일 때만 이벤트 전달
        super.observe(owner, Observer { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

    // call() 메서드는 어떤 스레드에서 호출되더라도 이벤트를 발생
    fun call() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            setValue(null)
        } else {  // 만약 메인 스레드가 아니라면 postValue를 사용
            pending.set(true)
            postValue(null)
        }
    }
}
