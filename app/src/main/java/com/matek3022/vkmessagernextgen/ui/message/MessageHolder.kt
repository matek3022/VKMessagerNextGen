package com.matek3022.vkmessagernextgen.ui.message

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
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
        text.text = item.text
        attachContainer.removeAllViews()
        item.attachments.forEach {
            it.photo?.let { photo ->
                val image = ImageView(attachContainer.context)
                image.setOnClickListener { imageClick?.invoke(photo) }
                attachContainer.addView(image)
                Glide.with(itemView.context).load(photo.getOriginalUrl())
                    .listener(object : SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL),
                        RequestListener<Drawable> {
                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return true
                        }

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return true
                        }

                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                        }
                    }).into(image)
            }
        }
        root.setOnClickListener {
            click?.invoke(item)
        }
    }
}