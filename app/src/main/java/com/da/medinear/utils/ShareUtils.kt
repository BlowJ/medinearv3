package com.da.medinear.utils

import android.content.Context
import com.google.gson.Gson

class ShareUtils(context: Context) {
    private val shared = context.getSharedPreferences(javaClass.name, Context.MODE_PRIVATE)

    fun setValue(value: String, key: String) {
        shared.edit().putString(key, value).commit()
    }

    fun getValue(key: String) = shared.getString(key, null)

    fun <T> setValue(value: T, key: String) {
        val json = Gson().toJson(value)
        shared.edit().putString(key, json).commit()
    }

    fun <T> getValue(key: String, clz: Class<T>) : T? {
        return try {
            val json = shared.getString(key, null)
            Gson().fromJson(json, clz)
        } catch (ex: Exception) {
            null
        }
    }

    fun logout() {
        shared.edit().remove(KEY_USER).commit()
    }

    companion object {
        const val KEY_USER = "key_user"
    }
}