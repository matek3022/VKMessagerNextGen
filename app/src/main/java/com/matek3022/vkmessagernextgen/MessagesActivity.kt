package com.matek3022.vkmessagernextgen

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matek3022.vkmessagernextgen.rxapi.model.User
import com.matek3022.vkmessagernextgen.rxapi.vm.MessagesViewModel
import com.matek3022.vkmessagernextgen.ui.message.MessageRVAdapter
import io.reactivex.disposables.Disposable

class MessagesActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "com.matek3022.vkmessagernextgen_extra_user_id"

        fun getIntent(context: Context, user: User?) = Intent(context, MessagesActivity::class.java).apply {
            putExtra(EXTRA_USER, user)
        }
    }

    lateinit var messageVM: MessagesViewModel
    lateinit var sendMessageBT: View
    lateinit var inputET: EditText
    lateinit var rv: RecyclerView
    val disposables = ArrayList<Disposable>()
    lateinit var adapter: MessageRVAdapter
    private var user: User? = null
    private var progress: ProgressDialog? = null

    var isStega = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        user = intent.getSerializableExtra(EXTRA_USER) as User?
        title = "${user?.firstName} ${user?.lastName}"
        messageVM = ViewModelProviders.of(this).get(MessagesViewModel::class.java)
        adapter = MessageRVAdapter(ArrayList(), user?.id ?: 0)
        rv = findViewById(R.id.list)
        sendMessageBT = findViewById(R.id.sendMessageBT)
        inputET = findViewById(R.id.inputMessageET)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this).apply { reverseLayout = true }
        disposables.add(messageVM.messagesSubject.subscribe { new ->
            (adapter.items as ArrayList).let { old ->
                old.clear()
                old.addAll(new)
            }
            adapter.notifyDataSetChanged()
        })
        if (adapter.itemCount == 0) update()

        sendMessageBT.setOnClickListener {
            progressChange(true, "Отправляю...")
            messageVM.sendMessage(this@MessagesActivity,
                userId = user?.id ?: 0,
                message = inputET.text.toString(),
                isCoded = isStega,
                stop = {
                    progressChange(false)
                    inputET.setText("")
                    update()
                })
        }
    }

    private fun progressChange(isProgress: Boolean, message: String = "") {
        if (progress == null) progress = ProgressDialog(this)
        progress?.let {
            it.setCancelable(false)
            it.setMessage(message)
            if (isProgress) it.show()
            else it.dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.message_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.update -> {
                update()
                true
            }
            R.id.settings -> {
                showStegaDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showStegaDialog() {
        val dialog = AlertDialog.Builder(this@MessagesActivity)
        dialog.setCancelable(false)
        dialog.setMessage("Включить/Выключить стеганографическое встраивание?")
        dialog.setPositiveButton("Вкл") { _, _ ->
            isStega = true
            update()
        }
        dialog.setNegativeButton("Выкл") { _, _ ->
            isStega = false
            update()
        }
        dialog.show()
    }

    private fun update() {
        messageVM.update(this, isStega, user?.id ?: 0, start = {
            progressChange(true, if (!isStega) "Обновляю..." else "Обновляю и декодирую...")
        },
            stop = {
                progressChange(false)
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.forEach { it.dispose() }
    }
}
