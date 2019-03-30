package com.matek3022.vkmessagernextgen.utils.stega

import android.graphics.Color


/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 29.03.19.
 */
fun getY(color: Int): Double {
    val r = Color.red(color)
    val g = Color.green(color)
    val b = Color.blue(color)
    return 0.299 * r + 0.587 * g + 0.114 * b
}

fun getCb(color: Int): Double {
    val r = Color.red(color)
    val g = Color.green(color)
    val b = Color.blue(color)
    return 128 - 0.168736 * r - 0.331264 * g + 0.5 * b
}

fun getCr(color: Int): Double {
    val r = Color.red(color)
    val g = Color.green(color)
    val b = Color.blue(color)
    return 128 + 0.5 * r - 0.418688 * g - 0.081312 * b
}
fun toRGB(y: Double, cb: Double, cr: Double): Int {
    val r: Double = y + 1.402 * (cr - 128)
    val g: Double = y - 0.344136 * (cb - 128) - 0.714136 * (cr - 128)
    val b: Double = y + 1.772 * (cb - 128)
    return Color.rgb(getNormalColor(r), getNormalColor(g), getNormalColor(b))
}

fun getNormalColor(color: Double): Int {
    val res = Math.round(color).toInt()
    if (res < 0) return 0
    if (res > 255) return 255
    return res
}