package com.matek3022.vkmessagernextgen.ui.base

import androidx.recyclerview.widget.RecyclerView

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
abstract class BaseRVAdapter<T: BaseRVHolder<B>, B>(val items: List<B>, val click:((item: B) -> Unit)?): RecyclerView.Adapter<T>() {

    final override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: T, position: Int) {
        holder.bind(items[position])
    }
}