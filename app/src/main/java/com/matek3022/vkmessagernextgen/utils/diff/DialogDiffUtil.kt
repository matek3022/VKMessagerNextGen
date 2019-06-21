package com.matek3022.vkmessagernextgen.utils.diff

import androidx.recyclerview.widget.DiffUtil
import com.matek3022.vkmessagernextgen.rxapi.result.ResultGetConversation

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 21.06.19.
 */
class DialogDiffUtil(
    private val oldDialogs: List<ResultGetConversation.Item>,
    private val newDialogs: List<ResultGetConversation.Item>): DiffUtil.Callback() {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int)=
        oldDialogs[oldItemPosition].conversation.peer.id == newDialogs[newItemPosition].conversation.peer.id

    override fun getOldListSize() = oldDialogs.size

    override fun getNewListSize() = newDialogs.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldDialogs[oldItemPosition] == newDialogs[newItemPosition]
}