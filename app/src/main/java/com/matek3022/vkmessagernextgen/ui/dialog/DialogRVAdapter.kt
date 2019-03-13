package com.matek3022.vkmessagernextgen.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.matek3022.vkmessagernextgen.R
import com.matek3022.vkmessagernextgen.rxapi.result.ResultGetConversation
import com.matek3022.vkmessagernextgen.ui.base.BaseRVAdapter

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
class DialogRVAdapter(items: List<ResultGetConversation.Item>, click:((ResultGetConversation.Item) -> Unit)? = null): BaseRVAdapter<DialogHolder, ResultGetConversation.Item>(items, click) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = DialogHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_dialog,
            null
        ),
        click
    )
}