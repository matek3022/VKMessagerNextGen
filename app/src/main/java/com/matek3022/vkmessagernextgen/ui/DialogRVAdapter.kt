package com.matek3022.vkmessagernextgen.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.matek3022.vkmessagernextgen.R
import com.matek3022.vkmessagernextgen.rxapi.model.ResultGetConv
import com.matek3022.vkmessagernextgen.ui.base.BaseRVAdapter

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
class DialogRVAdapter(items: List<ResultGetConv.Item>): BaseRVAdapter<DialogHolder, ResultGetConv.Item>(items) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = DialogHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_dialog, null))
}