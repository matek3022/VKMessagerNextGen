package com.matek3022.vkmessagernextgen.rxapi.vm

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import com.matek3022.vkmessagernextgen.App
import com.matek3022.vkmessagernextgen.R
import com.matek3022.vkmessagernextgen.rxapi.model.Message
import com.matek3022.vkmessagernextgen.rxapi.result.ResultSavePhoto
import com.matek3022.vkmessagernextgen.utils.stega.codeText
import com.matek3022.vkmessagernextgen.utils.stega.getText
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.*


/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 13.03.19.
 */
class MessagesViewModel : AbstractViewModel() {
    val messagesSubject: BehaviorSubject<List<Message>> = BehaviorSubject.create()

    fun decodeText(
        bitmap: Bitmap,
        start: (() -> Unit)? = null,
        stop: ((text: String?, e: Exception?) -> Unit)? = null
    ) {
        start?.invoke()
        launch {
            Observable.create<String> {
                try {
                    it.onNext(bitmap.getText())
                } catch (e: Exception) {
                    it.onError(Exception("Нет зашифрованного текста"))
                }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    stop?.invoke(it, null)
                }, {
                    stop?.invoke(null, Exception(it))
                })
        }
    }

    fun codeText(text: String, start: (() -> Unit)? = null, stop: ((b: Bitmap?, e: Exception?) -> Unit)? = null) {
        start?.invoke()
        launch {
            Observable.create<Bitmap> {
                val normBitmap = getBitmapToText(text)
                if (normBitmap == null) it.onError(Exception("Слишком большой текст, нет подходящей картинки для встраивания"))
                else {
                    normBitmap.codeText(text)
                    it.onNext(normBitmap)
                    it.onComplete()
                }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    stop?.invoke(it, null)
                }, {
                    stop?.invoke(null, Exception(it))
                })
        }
    }

    private fun getBitmapToText(text: String): Bitmap? {
        val resList = arrayListOf(
//            R.drawable.test1,
//            R.drawable.test2,
//            R.drawable.test3,
//            R.drawable.test4,
            R.drawable.test5//,
//            R.drawable.test6,
//            R.drawable.test7,
//            R.drawable.test9,
//            R.drawable.test11,
//            R.drawable.test13,
//            R.drawable.test14
        ).filter { isNormBitmap(it, text) }
        if (resList.isEmpty()) return null
        return getBitmapFromRes(resList[if (resList.size > 1) Random().nextInt(resList.size - 1) else 0])
    }

    private fun getBitmapFromRes(resId: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inMutable = true
        return BitmapFactory.decodeResource(App.instance.resources, resId, options)
    }

    private fun isNormBitmap(resId: Int, text: String): Boolean {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(App.instance.resources, resId, options)
        return text.toByteArray().size * 8 < Math.round(0.3 * (options.outWidth / 8) * (options.outHeight / 8)).toInt()
    }

    fun update(
        context: Context,
        isStega: Boolean,
        userId: Int,
        start: (() -> Unit)? = null,
        stop: (() -> Unit)? = null
    ) {
        start?.invoke()
        launch {
            App.instance.service.getMessages(userId = userId, count = if (isStega) 5 else 20).map {
                if (!isStega) {
                    it
                } else {
                    if (it.error != null) it
                    else {
                        it.response?.items = it.response?.items?.map { message ->
                            if (message.attachments.firstOrNull()?.photo != null) {
                                try {
                                    val url = URL(message.attachments.first().photo?.getOriginalUrl())
                                    val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                                    val textOut = bmp.getText()
                                    message.text = textOut
                                    message.attachments = arrayListOf()
                                } catch (e: Exception) {
                                    message.text = ""
                                }
                            } else {
                                message.text = ""
                            }
                            message
                        }?.filter { item ->
                            (item as? Message)?.text?.isNotEmpty() ?: false
                        } as List<Message>
                        it
                    }
                }
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { response ->
                    launchUI {
                        stop?.invoke()
                        response?.response?.items?.let {
                            messagesSubject.onNext(it)
                        }
                    }
                }
        }
    }

    fun uploadPhoto(
        context: Context,
        bitmap: Bitmap,
        start: (() -> Unit)? = null,
        stop: ((photo: ResultSavePhoto?) -> Unit)? = null
    ) {
        start?.invoke()
        launch {
            App.instance.service.getPhotoUploadServer().subscribe { responseServer ->
                responseServer?.response?.uploadUrl?.let { server ->
                    val f = File(context.cacheDir, "upload.jpg")
                    f.createNewFile()
                    val bos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                    val bitmapdata = bos.toByteArray()
                    val fos = FileOutputStream(f)
                    fos.write(bitmapdata)
                    fos.flush()
                    fos.close()
                    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f)
                    val body = MultipartBody.Part.createFormData("file1", "file1.jpg", requestFile)
                    launch {
                        App.instance.service.uploadPhoto(server, requestFile, body)
                            .subscribe { responseUploadPhoto ->
                                responseUploadPhoto?.let {
                                    launch {
                                        App.instance.service.savePhoto(
                                            photo = it.photo,
                                            server = it.server,
                                            hash = it.hash
                                        )
                                            .subscribe { listResponseSavePhoto ->
                                                launchUI {
                                                    stop?.invoke(listResponseSavePhoto?.response?.firstOrNull())
                                                }
                                            }
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

    fun sendMessage(
        context: Context,
        userId: Int,
        message: String,
        isCoded: Boolean,
        attachment: String? = null,
        start: (() -> Unit)? = null,
        stop: (() -> Unit)? = null
    ) {
        start?.invoke()
        if (isCoded) {
            codeText(message, stop = { bitmap, e ->
                e?.let {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    stop?.invoke()
                }
                bitmap?.let {
                    uploadPhoto(context, it, stop = {
                        if (it != null) {
                            sendMessage(
                                context,
                                userId = userId,
                                isCoded = false,
                                message = "",
                                attachment = "photo${it.ownerId}_${it.id}",
                                stop = {
                                    stop?.invoke()
                                })
                        } else {
                            stop?.invoke()
                        }
                    })
                }
            })
        } else launch {
            App.instance.service.messageSend(message = message, userId = userId, attachment = attachment)
                .subscribe {
                    launchUI { stop?.invoke() }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        messagesSubject.onComplete()
    }
}