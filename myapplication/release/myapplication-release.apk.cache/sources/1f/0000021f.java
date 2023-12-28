package com.my.blog.myapplication.util;

import android.content.Context;
import android.preference.PreferenceManager;
import k2.h;

/* loaded from: classes.dex */
public final class TestMyUtil {
    private String goods = "";
    private int index;

    public final String setMyGoods(Context context, String str) {
        h.r(context, "context");
        h.r(str, "good");
        this.goods = str;
        this.index++;
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("key " + this.goods + ' ' + this.index, String.valueOf(this.index)).commit();
        return "goods + " + this.index;
    }
}