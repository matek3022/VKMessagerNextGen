package com.matek3022.vkmessagernextgen.utils.diff

import androidx.recyclerview.widget.DiffUtil
import com.matek3022.vkmessagernextgen.rxapi.model.Message

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 21.06.19.
 */
class MessagesDiffUtil(
    private val oldMessages: List<Message>,
    private val newMessages: List<Message>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldMessages[oldItemPosition].id == newMessages[newItemPosition].id

    override fun getOldListSize() = oldMessages.size

    override fun getNewListSize() = newMessages.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldMessages[oldItemPosition] == newMessages[newItemPosition]
}