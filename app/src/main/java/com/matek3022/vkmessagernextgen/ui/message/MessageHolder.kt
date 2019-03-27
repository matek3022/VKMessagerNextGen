package com.matek3022.vkmessagernextgen.ui.message

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.matek3022.vkmessagernextgen.R
import com.matek3022.vkmessagernextgen.rxapi.model.Attachment
import com.matek3022.vkmessagernextgen.rxapi.model.Message
import com.matek3022.vkmessagernextgen.ui.base.BaseRVHolder
import com.matek3022.vkmessagernextgen.utils.convertToTime
import com.matek3022.vkmessagernextgen.utils.stega.getText

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
    fun bind(item: Message, isStega: Boolean) {
        title.text = convertToTime(item.date)
        if (isStega) text.visibility = View.GONE
        else text.visibility = View.VISIBLE
        text.text = item.text
        attachContainer.removeAllViews()
        item.attachments.forEach {
            it.photo?.let { photo ->
                if (isStega) {
                    val textView = TextView(attachContainer.context).apply { visibility = View.GONE }
                    attachContainer.addView(textView)
                    Glide.with(itemView.context).asBitmap().load(photo.getOriginalUrl())
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                try {
                                    val textOut = resource.getText()
                                    textView.text = textOut
                                    textView.visibility = View.VISIBLE
                                } catch (e: Exception) {}
                            }
                        })
                } else {
                    val image = ImageView(attachContainer.context).apply { adjustViewBounds = true }
                    image.setOnClickListener { imageClick?.invoke(photo) }
                    attachContainer.addView(image)
                    Glide.with(itemView.context).load(photo.getOriginalUrl()).into(image)
                }
            }
        }
        root.setOnClickListener {
            click?.invoke(item)
        }
    }
}