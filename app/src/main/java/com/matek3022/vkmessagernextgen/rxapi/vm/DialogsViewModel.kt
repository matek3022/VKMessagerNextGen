package com.matek3022.vkmessagernextgen.rxapi.vm

import android.content.Context
import com.matek3022.vkmessagernextgen.App
import com.matek3022.vkmessagernextgen.rxapi.result.ResultGetConversation
import io.reactivex.subjects.BehaviorSubject

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
class DialogsViewModel : AbstractViewModel() {
    val dialogsSubject: BehaviorSubject<List<ResultGetConversation.Item>> = BehaviorSubject.create()

    fun update(context: Context, start: (() -> Unit)? = null, stop: (() -> Unit)? = null) {
        start?.invoke()
        launch {
            App.instance.service.getDialogs().subscribe { response ->
                response?.response?.processUserToItem()
                launchUI {
                    stop?.invoke()
                    response?.response?.items?.let {
                        dialogsSubject.onNext(it)
                    }
                }

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        dialogsSubject.onComplete()
    }
}