package com.matek3022.vkmessagernextgen.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
abstract class BaseRVHolder<T>(itemView: View, val click:((item: T) -> Unit)?): RecyclerView.ViewHolder(itemView) {
    open fun bind(item: T) {

    }
}