package com.matek3022.vkmessagernextgen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.matek3022.vkmessagernextgen.rxapi.vm.DialogsViewModel
import com.matek3022.vkmessagernextgen.ui.dialog.DialogRVAdapter
import com.matek3022.vkmessagernextgen.utils.stega.codeText
import com.matek3022.vkmessagernextgen.utils.stega.computeSF
import com.matek3022.vkmessagernextgen.utils.stega.generateTextToPercentage
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_base.*

class BaseActivity : AppCompatActivity() {

    lateinit var dialogsViewModel: DialogsViewModel
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var rv: RecyclerView
    val disposables = ArrayList<Disposable>()
    lateinit var adapter: DialogRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        title = "Диалоги"
//        test()
//        testSF()
//        megaTest()
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
                if (old != new) {
                    if (old.size == 0) {
                        TransitionManager.beginDelayedTransition(mainRoot, Slide(Gravity.BOTTOM))
                    }
                    old.clear()
                    old.addAll(new)
                    adapter.notifyDataSetChanged()
                }
            }
        })
        if (adapter.itemCount == 0) update()
        swipeRefreshLayout.setOnRefreshListener { update() }
    }

    fun testSF() {
        val res = computeSF(BitmapFactory.decodeResource(resources, R.drawable.test1, BitmapFactory.Options().apply { inPreferredConfig = Bitmap.Config.ARGB_8888 }), BitmapFactory.decodeResource(resources, R.drawable.test2, BitmapFactory.Options().apply { inPreferredConfig = Bitmap.Config.ARGB_8888 }))
        return
    }

    fun test() {
        val resList = ArrayList<Pair<Int, Double>>()
        for (i in 1..100) {
            resList.add(i to getPsnrFromPercentage(i))
        }
        return
    }

    fun megaTest() {
        val resList = ArrayList<Double>()
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inMutable = true
        val bitmapa = BitmapFactory.decodeResource(resources, R.drawable.test1, options)
        for (i in 44..100) {//43 уже посчиталось
            val stageList = ArrayList<Double>()
            for (j in 1..100) {
                stageList.add(getPsnrFromPercentage(i, bitmapa))
            }
            val sr = stageList.toDoubleArray().sum() / 100
            Log.wtf("tag_iter", "$i${if (i < 10) " " else ""} SF with 100 iter per each = ${sr.toString().replace(".", ",")}")
            resList.add(sr)
        }
        return
    }

    fun getPsnrFromPercentage(percentage: Int, btmp: Bitmap? = null): Double{
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inMutable = true
        var cw = if (btmp != null) btmp.copy(Bitmap.Config.ARGB_8888, true) else BitmapFactory.decodeResource(resources, R.drawable.test1, options)
        var c = if (btmp != null) btmp.copy(Bitmap.Config.ARGB_8888, false) else cw.copy(Bitmap.Config.ARGB_8888, false)
        val text = generateTextToPercentage(cw, percentage)
        cw.codeText(text)
        val res = computeSF(cw, c)
        Log.wtf("tag_percentage", "$percentage${if (percentage < 10) " " else ""} SF = ${res.toString().replace(".", ",")}")

//        val res = computeSF(c, cw)
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
