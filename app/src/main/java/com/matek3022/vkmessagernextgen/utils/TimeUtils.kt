package com.matek3022.vkmessagernextgen.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 12.03.19.
 */
fun convertToTime(unixTime: Int): String {
    val dv = unixTime * 1000L
    val df = Date(dv)
    val year = SimpleDateFormat("yyyy", Locale.ENGLISH).format(df)
    val month = SimpleDateFormat("MM", Locale.ENGLISH).format(df)
    val day = SimpleDateFormat("dd", Locale.ENGLISH).format(df)

    val currTime = Date(System.currentTimeMillis())
    val curryear = SimpleDateFormat("yyyy", Locale.ENGLISH).format(currTime)
    val currmonth = SimpleDateFormat("MM", Locale.ENGLISH).format(currTime)
    val currday = SimpleDateFormat("dd", Locale.ENGLISH).format(currTime)

    if (year == curryear) {
        if (month == currmonth) {
            if (day == currday) {
                return SimpleDateFormat("hh:mm", Locale.ENGLISH).format(df)
            } else return day
        } else return month
    } else return year
}