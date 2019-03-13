package com.matek3022.vkmessagernextgen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matek3022.vkmessagernextgen.rxapi.vm.MessagesViewModel
import com.matek3022.vkmessagernextgen.ui.message.MessageRVAdapter
import io.reactivex.disposables.Disposable

class MessagesActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_ID = "extra_user_id"

        fun getIntent(context: Context, userId: Int) = Intent(context, MessagesActivity::class.java).apply {
            putExtra(EXTRA_USER_ID, userId)
        }
    }

    lateinit var messageVM: MessagesViewModel
    lateinit var rv: RecyclerView
    val disposables = ArrayList<Disposable>()
    lateinit var adapter: MessageRVAdapter
    var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        userId = intent.getIntExtra(EXTRA_USER_ID, 0)
        adapter = MessageRVAdapter(ArrayList(), userId)
        rv = findViewById(R.id.list)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        messageVM = ViewModelProviders.of(this).get(MessagesViewModel::class.java)
        disposables.add(messageVM.messagesSubject.subscribe {new ->
            (adapter.items as ArrayList).let { old ->
                old.clear()
                old.addAll(new)
            }
            adapter.notifyDataSetChanged()
        })
        if (adapter.itemCount == 0) update()
    }

    private fun update() {
        messageVM.update(this, userId)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.forEach { it.dispose() }
    }
}
