package com.matek3022.vkmessagernextgen.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

/**
 * Скрываем клавиатуру
 *
 * @param v любая [View]
 */
fun hideKeyboard(v: View?) {
    if (v != null && v.context != null) {
        if (!v.isFocused) {
            v.requestFocus()
        }
        val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
        imm.hideSoftInputFromInputMethod(v.windowToken, 0)
    }
}

fun hideKeyboard(activity: Activity?) {
    if (activity != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (activity.currentFocus != null && imm != null) {
            imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            imm.hideSoftInputFromInputMethod(activity.currentFocus!!.windowToken, 0)
        }
    }
}

fun showKeyboard(v: View?) {
    if (v != null && v.context != null) {
        if (!v.isFocused) {
            v.requestFocus()
        }
        val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}

fun openSystemBrowser(context: Context?, url: String) {
    if (context != null) {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        } catch (e: Exception) {
            showToast(context, "Нет приложения для обработки запроса")
        }

    }
}

fun showToast(context: Context?, mess: String) {
    if (context != null) {
        Toast.makeText(context, mess, Toast.LENGTH_SHORT).show()
    }
}

fun displayUnknownError(context: Context?) {
    if (context == null) return
    val builder = AlertDialog.Builder(context)
    builder.setMessage("Произошла непредвиденная ошибка, попробуйте повторить операцию позже")
    builder.setCancelable(false)
    builder.setPositiveButton("OK") { dialog, which -> }
    try {
        builder.show()
    } catch (ignored: WindowManager.BadTokenException) {
    }

}

fun displayUnknownError(context: Context?, okListener: DialogInterface.OnClickListener?) {
    if (context == null) return
    val builder = AlertDialog.Builder(context)
    builder.setMessage("Произошла непредвиденная ошибка, попробуйте повторить операцию позже")
    builder.setPositiveButton("OK", okListener)
    builder.setCancelable(false)
    try {
        builder.show()
    } catch (ignored: WindowManager.BadTokenException) {
    }

}


fun displayOkMessage(context: Context?, message: Int, okListener: DialogInterface.OnClickListener?) {
    if (context == null) return
    displayOkMessage(context, context.getString(message), okListener)
}

fun displayOkMessage(context: Context?, message: Int, title: Int, okListener: DialogInterface.OnClickListener?) {
    if (context == null) return
    displayOkMessage(context, context.getString(message), context.getString(title), okListener)
}

fun displayOkbuilder(
    context: Context?,
    message: String?,
    okListener: DialogInterface.OnClickListener??
): AlertDialog.Builder? {
    if (context == null) return null
    val builder = AlertDialog.Builder(context)
    builder.setMessage(message)
    builder.setPositiveButton("OK", okListener)
    return builder
}

fun displayCencalebleOkMessage(context: Context, message: String, okListener: DialogInterface.OnClickListener?) {
    val builder = displayOkbuilder(context, message, okListener)
    if (builder != null) {
        builder.setCancelable(false)
        try {
            builder.show()
        } catch (ignored: WindowManager.BadTokenException) {
            ignored.printStackTrace()
        }

    }
}

fun displayOkMessage(context: Context, message: String, okListener: DialogInterface.OnClickListener?) {
    val builder = displayOkbuilder(context, message, okListener)
    if (builder != null) {
        try {
            builder.show()
        } catch (ignored: WindowManager.BadTokenException) {
            ignored.printStackTrace()
        }

    }
}

fun displayOkMessage(context: Context, message: String, title: String, okListener: DialogInterface.OnClickListener?) {
    val builder = displayOkbuilder(context, message, okListener)
    if (builder != null) {
        builder.setTitle(title)
        try {
            builder.show()
        } catch (ignored: WindowManager.BadTokenException) {
        }

    }
}

fun displayAreYouSureDialogTitle(
    context: Context?,
    message: String,
    title: String,
    okListener: DialogInterface.OnClickListener?
) {
    if (context == null) return
    val builder = AlertDialog.Builder(context)
    builder.setMessage(message)
    builder.setTitle(title)
    builder.setPositiveButton("Да", okListener)
    builder.setNegativeButton("Нет", okListener)
    builder.create().show()
}

fun displayYesOrNotDialog(
    context: Context?,
    messageId: Int,
    okListener: DialogInterface.OnClickListener?,
    notListener: DialogInterface.OnClickListener?
) {
    if (context == null) return
    displayYesOrNotDialog(context, context.getString(messageId), okListener, notListener)
}

fun displayYesOrNotDialog(
    context: Context?,
    message: String,
    okListener: DialogInterface.OnClickListener?,
    notListener: DialogInterface.OnClickListener?
) {
    if (context == null) return
    val builder = AlertDialog.Builder(context)
    builder.setMessage(message)
    builder.setPositiveButton("Да", okListener)
    builder.setNegativeButton("Нет", notListener)
    builder.create().show()
}

fun displayYesOrNotDialog(
    context: Context?,
    message: String,
    positiveButton: String,
    negativeButton: String,
    okListener: DialogInterface.OnClickListener?,
    notListener: DialogInterface.OnClickListener?
) {
    if (context == null) return
    val builder = AlertDialog.Builder(context)
    builder.setMessage(message)
    builder.setPositiveButton(positiveButton, okListener)
    builder.setNegativeButton(negativeButton, notListener)
    builder.create().show()
}

fun displayYesOrNotTitleDialog(
    context: Context?,
    title: String,
    message: String,
    okListener: DialogInterface.OnClickListener?,
    notListener: DialogInterface.OnClickListener?
) {
    if (context == null) return
    val builder = AlertDialog.Builder(context)
    builder.setMessage(message)
    builder.setTitle(title)
    builder.setPositiveButton("Да", okListener)
    builder.setNegativeButton("Нет", notListener)
    builder.create().show()
}