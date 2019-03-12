package com.matek3022.vkmessagernextgen.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.matek3022.vkmessagernextgen.R
import com.matek3022.vkmessagernextgen.rxapi.model.ResultGetConv
import com.matek3022.vkmessagernextgen.ui.base.BaseRVHolder
import com.matek3022.vkmessagernextgen.utils.convertToTime

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
class DialogHolder(itemView: View): BaseRVHolder<ResultGetConv.Item>(itemView) {

    val avatar = itemView.findViewById<ImageView>(R.id.avatarIV)
    val personName = itemView.findViewById<TextView>(R.id.personNameTV)
    val lastMessageTV = itemView.findViewById<TextView>(R.id.lastMessageTV)
    val timeTV = itemView.findViewById<TextView>(R.id.timeTV)
    val messageCountTV = itemView.findViewById<TextView>(R.id.messageCountTV)
    val onlineIdentifier = itemView.findViewById<View>(R.id.onlineIdentifierIV)
    val typing = itemView.findViewById<View>(R.id.typing_tv)

    override fun bind(item: ResultGetConv.Item) {
        timeTV.text = convertToTime(item.lastMessage.date)
        Glide.with(itemView.context).load(item.user?.photoUrlOrig200).into(avatar)
        personName.text = item.user?.firstName ?: "" + item.user?.lastName ?: ""
        lastMessageTV.text = item.lastMessage.text
        if (item.conversation.unreadMessageCount > 0) {
            messageCountTV.visibility = View.VISIBLE
            messageCountTV.text = item.conversation.unreadMessageCount.toString()
        } else messageCountTV.visibility = View.GONE
        onlineIdentifier.visibility = if (item.user?.online ?: 0 == 1) View.VISIBLE else View.GONE
    }
}