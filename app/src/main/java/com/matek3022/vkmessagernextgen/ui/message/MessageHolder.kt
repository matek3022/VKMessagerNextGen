package com.matek3022.vkmessagernextgen.ui.message

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.matek3022.vkmessagernextgen.R
import com.matek3022.vkmessagernextgen.rxapi.model.Attachment
import com.matek3022.vkmessagernextgen.rxapi.model.Message
import com.matek3022.vkmessagernextgen.ui.base.BaseRVHolder
import com.matek3022.vkmessagernextgen.utils.convertToTime

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 13.03.19.
 */
class MessageHolder(
    itemView: View,
    click: ((item: Message) -> Unit)?,
    val imageClick: ((photo: Attachment.Photo) -> Unit)? = null
) : BaseRVHolder<Message>(itemView, click) {

    private val title = itemView.findViewById<TextView>(R.id.titleTV)
    private val text = itemView.findViewById<TextView>(R.id.textTV)
    private val attachContainer = itemView.findViewById<LinearLayout>(R.id.attachContainer)
    private val root = itemView.findViewById<View>(R.id.root)

    override fun bind(item: Message) {
        title.text = convertToTime(item.date)
        if (item.text.isEmpty()) text.visibility = View.GONE
        else text.visibility = View.VISIBLE
        text.text = item.text
        attachContainer.visibility = View.GONE
        attachContainer.removeAllViews()
        item.attachments.forEach {
            if (it.photo != null) attachContainer.visibility = View.VISIBLE
            it.photo?.let { photo ->
                val image = ImageView(attachContainer.context).apply { adjustViewBounds = true }
                image.setOnClickListener { imageClick?.invoke(photo) }
                attachContainer.addView(image)
                Glide.with(itemView.context).load(photo.getOriginalUrl()).into(image)
            }
        }
        root.setOnClickListener {
            click?.invoke(item)
        }
        root.requestLayout()
    }
}