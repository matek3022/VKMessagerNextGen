package com.matek3022.vkmessagernextgen.ui.message

import android.view.LayoutInflater
import android.view.ViewGroup
import com.matek3022.vkmessagernextgen.R
import com.matek3022.vkmessagernextgen.rxapi.model.Message
import com.matek3022.vkmessagernextgen.ui.base.BaseRVAdapter

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 13.03.19.
 */
class MessageRVAdapter(items: List<Message>, val currPeerId: Int,
                       click:((item: Message) -> Unit)? = null): BaseRVAdapter<MessageHolder, Message>(items, click) {

    /**
     * 1 - исходящие
     * 0 - входящие
     */
    override fun getItemViewType(position: Int): Int {
        return if (items[position].fromId == currPeerId) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MessageHolder(
        LayoutInflater.from(parent.context).inflate(
            if (viewType == 0) R.layout.item_message_inbox else R.layout.item_message_outbox,
            null
        ),
        click
    )
}