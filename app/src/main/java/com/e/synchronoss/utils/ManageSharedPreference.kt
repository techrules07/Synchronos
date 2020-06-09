package com.e.synchronoss.utils

import android.content.Context
import android.content.SharedPreferences

class ManageSharedPreference {

    private var mManageSharedPreferences: ManageSharedPreference? = null

    private fun ManageSharedPreferences() {}

    @Synchronized
    fun newInstance(): ManageSharedPreference? {
        if (mManageSharedPreferences == null) {
            mManageSharedPreferences = ManageSharedPreference()
        }
        return mManageSharedPreferences
    }

    fun saveString(context: Context, text: String?, key: String?) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor
        settings = context.getSharedPreferences(key, Context.MODE_PRIVATE) //1
        editor = settings.edit() //2
        editor.putString(key, text) //3
        editor.commit() //4
    }

    fun getString(context: Context, key: String?): String? {
        val settings: SharedPreferences
        val text: String?
        settings = context.getSharedPreferences(key, Context.MODE_PRIVATE) //1
        text = settings.getString(key, null) //2
        return text
    }

}