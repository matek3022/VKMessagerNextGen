package com.matek3022.vkmessagernextgen.rxapi.vm

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.matek3022.vkmessagernextgen.App
import com.matek3022.vkmessagernextgen.R
import com.matek3022.vkmessagernextgen.rxapi.model.Message
import com.matek3022.vkmessagernextgen.rxapi.result.ResultSavePhoto
import com.matek3022.vkmessagernextgen.utils.stega.codeText
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 13.03.19.
 */
class MessagesViewModel : AbstractViewModel() {
    val messagesSubject: BehaviorSubject<List<Message>> = BehaviorSubject.create()
    val photoUploaded: BehaviorSubject<ResultSavePhoto> = BehaviorSubject.create()

    fun codeText(text: String, start: (() -> Unit)? = null, stop: ((b: Bitmap?, e: Exception?) -> Unit)? = null) {
        start?.invoke()
        launch {
            Observable.create<Any> {
                val normBitmap = getBitmapToText(text)
                if (normBitmap == null) launchUI { stop?.invoke(null, Exception("Слишком большой текст, нет подходящей картинки для встраивания")) }
                normBitmap?.let {
                    it.codeText(text)
                    launchUI { stop?.invoke(it, null) }
                }
            }.subscribe()
        }
    }

    private fun getBitmapToText(text: String): Bitmap? {
        val resList = arrayListOf(R.drawable.test1,
                R.drawable.test2,
                R.drawable.test3,
                R.drawable.test4,
                R.drawable.test5,
                R.drawable.test6,
                R.drawable.test7).filter { isNormBitmap(it, text) }
        if (resList.isEmpty()) return null
        return getBitmapFromRes(resList[Random().nextInt(resList.size - 1)])
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

    fun update(context: Context, userId: Int, start: (() -> Unit)? = null, stop: (() -> Unit)? = null) {
        start?.invoke()
        launch {
            App.instance.service.getMessages(userId = userId).subscribe { response ->
                launchUI {
                    stop?.invoke()
                    response?.response?.items?.let {
                        messagesSubject.onNext(it)
                    }
                }
            }
        }
    }

    fun uploadPhoto(context: Context, bitmap: Bitmap, start: (() -> Unit)? = null, stop: (() -> Unit)? = null) {
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
//                    val description = RequestBody.create(MediaType.parse("multipart/form-data", "file1"))
                    launch {
                        App.instance.service.uploadPhoto(server, requestFile, body).subscribe { responseUploadPhoto ->
                            responseUploadPhoto?.let {
                                launch {
                                    App.instance.service.savePhoto(photo = it.photo, server = it.server, hash = it.hash)
                                        .subscribe { listResponseSavePhoto ->
                                            launchUI {
                                                stop?.invoke()
                                                listResponseSavePhoto?.response?.first()?.let {
                                                    photoUploaded.onNext(it)
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
    }

    fun sendMessage(
        context: Context,
        userId: Int,
        message: String,
        attachment: String,
        start: (() -> Unit)? = null,
        stop: (() -> Unit)? = null
    ) {
        start?.invoke()
        launch {
            App.instance.service.messageSend(message = message, userId = userId, attachment = attachment).subscribe {
                launchUI { stop?.invoke() }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        messagesSubject.onComplete()
    }
}