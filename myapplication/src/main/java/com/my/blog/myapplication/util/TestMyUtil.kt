package com.my.blog.myapplication.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.SharedPreferencesCompat

class TestMyUtil {
    private var goods: String = ""
    private var index = 0;

    fun setMyGoods(context: Context, good: String): String {
        goods = good
        index++
        val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        defaultSharedPreferences.edit().putString("key $goods $index", index.toString()).commit()
        return "goods + $index";
    }
}