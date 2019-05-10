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
                return SimpleDateFormat("HH:mm", Locale.ENGLISH).format(df)
            } else return "$day ${getMonthFromInt(month)}"
        } else return getMonth(month)
    } else return year
}

private fun getMonthFromInt(month: String) = when(month.toInt()) {
    0 -> "Января"
    1 -> "Февраля"
    2 -> "Марта"
    3 -> "Апреля"
    4 -> "Мая"
    5 -> "Июня"
    6 -> "Июля"
    7 -> "Августа"
    8 -> "Сентября"
    9  -> "Ноября"
    10 -> "Октября"
    11 -> "Декабря"
    else -> "Неизвестного месяца"
}

private fun getMonth(month: String) = when(month.toInt()) {
    0 -> "Январь"
    1 -> "Февраль"
    2 -> "Март"
    3 -> "Апрель"
    4 -> "Май"
    5 -> "Июнь"
    6 -> "Июль"
    7 -> "Август"
    8 -> "Сентябрь"
    9  -> "Ноябрь"
    10 -> "Октябрь"
    11 -> "Декабрь"
    else -> "Неизвестный месяц"
}