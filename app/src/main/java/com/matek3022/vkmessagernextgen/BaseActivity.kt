package com.matek3022.vkmessagernextgen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.matek3022.vkmessagernextgen.rxapi.vm.DialogsViewModel
import com.matek3022.vkmessagernextgen.ui.dialog.DialogRVAdapter
import com.matek3022.vkmessagernextgen.utils.stega.codeText
import com.matek3022.vkmessagernextgen.utils.stega.computePsnr
import com.matek3022.vkmessagernextgen.utils.stega.computeSF
import com.matek3022.vkmessagernextgen.utils.stega.generateTextToPercentage
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
//        test()
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
        val resList = ArrayList<Pair<Int, Double>>()
        for (i in 1..100) {
            resList.add(i to getPsnrFromPercentage(i))
        }
        return
    }

    fun getPsnrFromPercentage(percentage: Int): Double{
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inMutable = true
        var cw = BitmapFactory.decodeResource(resources, R.drawable.test7, options)
        var c = cw.copy(Bitmap.Config.ARGB_8888, false)
        val text = generateTextToPercentage(cw, percentage)
        cw.codeText(text)
        Log.wtf("tag_percentage", percentage.toString())
//        val res = computePsnr(cw, c)
        val res = computeSF(c, cw)
        cw.recycle()
        c.recycle()
        return res
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
