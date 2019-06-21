package com.matek3022.vkmessagernextgen

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matek3022.vkmessagernextgen.rxapi.model.User
import com.matek3022.vkmessagernextgen.rxapi.vm.MessagesViewModel
import com.matek3022.vkmessagernextgen.ui.message.MessageRVAdapter
import com.matek3022.vkmessagernextgen.utils.PreferencesManager
import com.matek3022.vkmessagernextgen.utils.diff.MessagesDiffUtil
import com.matek3022.vkmessagernextgen.utils.displayOkMessage
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_messages.*

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
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        user = intent.getSerializableExtra(EXTRA_USER) as User?
        supportActionBar?.title = "${user?.firstName} ${user?.lastName}"
        messageVM = ViewModelProviders.of(this).get(MessagesViewModel::class.java)
        adapter = MessageRVAdapter(ArrayList(), user?.id ?: 0)
        rv = findViewById(R.id.list)
        sendMessageBT = findViewById(R.id.sendMessageBT)
        inputET = findViewById(R.id.inputMessageET)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this).apply { reverseLayout = true }
        disposables.add(messageVM.messagesSubject.subscribe { new ->
            (adapter.items as ArrayList).let { old ->
                val dialogDiffUtil = MessagesDiffUtil(old, new)
                val diffResult = DiffUtil.calculateDiff(dialogDiffUtil, true)
                adapter.items = new
                diffResult.dispatchUpdatesTo(adapter)
                rv.scrollToPosition(0)
            }
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
        val view = layoutInflater.inflate(R.layout.layout_message_settings, null)
        val inputKeyET = view.findViewById<EditText>(R.id.inputKeyDialog)
        val descriptionText = view.findViewById<TextView>(R.id.textDialog)
        inputKeyET.isEnabled = !isStega
        if (isStega) descriptionText.text = "Текущий используемый ключ для шифрования информации в изображении"
        inputKeyET.setText(PreferencesManager.getCryptKeyById(user?.id ?: 0))
        dialog.setView(view)
        dialog.setPositiveButton(if (isStega) "Выкл" else "Вкл") { _, _ ->
            if (!isStega && inputKeyET.text.isEmpty()) {
                displayOkMessage(this@MessagesActivity, "Ключ не может быть пустым", null)
                return@setPositiveButton
            }
            PreferencesManager.setCryptKeyById(user?.id ?: 0, inputKeyET.text.toString())
            isStega = !isStega
            update()
        }
        dialog.setNegativeButton("Отмена") { _, _ ->
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
