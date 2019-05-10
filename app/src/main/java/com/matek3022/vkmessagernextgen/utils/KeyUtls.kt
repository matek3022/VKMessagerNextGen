package com.matek3022.vkmessagernextgen.utils

/**
 * ключ формата: "ключДляЧата_айдиЧата_мойАйди"
 */
fun getMyKey(chatUserId: Int) = "${PreferencesManager.getCryptKeyById(chatUserId)}_${chatUserId}_${PreferencesManager.getUserID()}"

/**
 * ключ формата: "ключДляЧата_мойАйди_айдиЧата"
 */
fun getOtherKey(chatUserId: Int) = "${PreferencesManager.getCryptKeyById(chatUserId)}_${PreferencesManager.getUserID()}_$chatUserId"