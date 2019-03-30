package com.matek3022.vkmessagernextgen

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.matek3022.vkmessagernextgen.rxapi.vm.DialogsViewModel
import com.matek3022.vkmessagernextgen.ui.dialog.DialogRVAdapter
import com.matek3022.vkmessagernextgen.utils.stega.getCb
import com.matek3022.vkmessagernextgen.utils.stega.getCr
import com.matek3022.vkmessagernextgen.utils.stega.getY
import com.matek3022.vkmessagernextgen.utils.stega.toRGB
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
        test()
        adapter = DialogRVAdapter(ArrayList()) {
            startActivity(MessagesActivity.getIntent(this, it.user))
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

    fun test() {
        val yellow = Color.BLUE
        val r = Color.red(Color.BLUE)
        val g  = Color.green(Color.BLUE)
        val b = Color.blue(Color.BLUE)

        val y = getY(Color.BLUE)
        val cr = getCr(Color.BLUE)
        val cb = getCb(Color.BLUE)

        val ycrcbYellow = toRGB(y, cb, cr)

        val rgbYellow = Color.rgb(r,g,b)
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
