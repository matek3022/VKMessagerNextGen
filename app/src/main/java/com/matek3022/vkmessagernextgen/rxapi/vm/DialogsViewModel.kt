package com.matek3022.vkmessagernextgen.rxapi.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import com.matek3022.vkmessagernextgen.App
import com.matek3022.vkmessagernextgen.rxapi.base.ServerResponse
import com.matek3022.vkmessagernextgen.rxapi.model.ResultGetConv
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
class DialogsViewModel: ViewModel() {
    val dialogsSubject: BehaviorSubject<List<ResultGetConv.Item>> = BehaviorSubject.create()

    fun update(context: Context, start: (() -> Unit)? = null, stop: (() -> Unit)? = null) {
        start?.invoke()
        App.instance.service.getDialogs().enqueue(object : Callback<ServerResponse<ResultGetConv>> {
            override fun onFailure(call: Call<ServerResponse<ResultGetConv>>, t: Throwable) {
                stop?.invoke()
            }

            override fun onResponse(
                call: Call<ServerResponse<ResultGetConv>>,
                response: Response<ServerResponse<ResultGetConv>>
            ) {
                response.body()?.response?.processUserToItem()
                stop?.invoke()
                response.body()?.response?.items?.let {
                    dialogsSubject.onNext(it)
                }
            }

        })
    }

    override fun onCleared() {
        super.onCleared()
        dialogsSubject.onComplete()
    }
}