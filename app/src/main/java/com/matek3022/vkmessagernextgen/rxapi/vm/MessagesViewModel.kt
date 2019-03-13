package com.matek3022.vkmessagernextgen.rxapi.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import com.matek3022.vkmessagernextgen.App
import com.matek3022.vkmessagernextgen.rxapi.base.ServerResponse
import com.matek3022.vkmessagernextgen.rxapi.model.Message
import com.matek3022.vkmessagernextgen.rxapi.result.ResultGetMessages
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 13.03.19.
 */
class MessagesViewModel: ViewModel() {
    val messagesSubject: BehaviorSubject<List<Message>> = BehaviorSubject.create()

    fun update(context: Context, userId: Int, start: (() -> Unit)? = null, stop: (() -> Unit)? = null) {
        start?.invoke()
        App.instance.service.getMessages(userId = userId).enqueue(object : Callback<ServerResponse<ResultGetMessages>> {
            override fun onFailure(call: Call<ServerResponse<ResultGetMessages>>, t: Throwable) {
                stop?.invoke()
            }

            override fun onResponse(
                call: Call<ServerResponse<ResultGetMessages>>,
                response: Response<ServerResponse<ResultGetMessages>>
            ) {
                stop?.invoke()
                response.body()?.response?.items?.let {
                    messagesSubject.onNext(it)
                }
            }

        })
    }

    override fun onCleared() {
        super.onCleared()
        messagesSubject.onComplete()
    }
}