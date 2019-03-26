package com.matek3022.vkmessagernextgen.rxapi.vm

import android.content.Context
import android.graphics.Bitmap
import com.matek3022.vkmessagernextgen.App
import com.matek3022.vkmessagernextgen.rxapi.model.Message
import com.matek3022.vkmessagernextgen.rxapi.result.ResultSavePhoto
import io.reactivex.subjects.BehaviorSubject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 13.03.19.
 */
class MessagesViewModel : AbstractViewModel() {
    val messagesSubject: BehaviorSubject<List<Message>> = BehaviorSubject.create()
    val photoUploaded: BehaviorSubject<ResultSavePhoto> = BehaviorSubject.create()

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
                                    App.instance.service.savePhoto(photo = it.photo, server = it.server, hash = it.hash).subscribe { listResponseSavePhoto ->
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