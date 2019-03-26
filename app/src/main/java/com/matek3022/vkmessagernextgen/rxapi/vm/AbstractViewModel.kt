package com.matek3022.vkmessagernextgen.rxapi.vm

import android.os.Handler
import android.os.Looper
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 26.03.19.
 */
abstract class AbstractViewModel : ViewModel() {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val handler = Handler(Looper.getMainLooper())

    fun launch(job: () -> Disposable) {
        disposables.add(job())
    }

    fun launchUI(job: () -> Unit) {
        handler.post { job.invoke() }
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}
