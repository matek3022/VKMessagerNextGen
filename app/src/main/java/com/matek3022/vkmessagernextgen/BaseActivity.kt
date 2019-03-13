package com.matek3022.vkmessagernextgen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.matek3022.vkmessagernextgen.rxapi.vm.DialogsViewModel
import com.matek3022.vkmessagernextgen.ui.dialog.DialogRVAdapter
import io.reactivex.disposables.Disposable

class BaseActivity : AppCompatActivity() {

    lateinit var dialogsViewModel: DialogsViewModel
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var rv: RecyclerView
    val disposables = ArrayList<Disposable>()
    lateinit var adapter: DialogRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        adapter = DialogRVAdapter(ArrayList()) {
            startActivity(MessagesActivity.getIntent(this, it.conversation.peer.id))
        }
        rv = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRL)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        dialogsViewModel = ViewModelProviders.of(this).get(DialogsViewModel::class.java)
        disposables.add(dialogsViewModel.dialogsSubject.subscribe {new ->
            (adapter.items as ArrayList).let { old ->
                old.clear()
                old.addAll(new)
            }
            adapter.notifyDataSetChanged()
        })
        if (adapter.itemCount == 0) update()
        swipeRefreshLayout.setOnRefreshListener { update() }
    }

    private fun update() {
        dialogsViewModel.update(this,
            {swipeRefreshLayout.isRefreshing = true},
            {swipeRefreshLayout.isRefreshing = false})
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.forEach { it.dispose() }
    }
}
