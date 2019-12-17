package com.zaze.hook.xposed

import com.google.gson.GsonBuilder

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-17 - 14:13
 */
object Utils {
    private val gsonBuilder = GsonBuilder()

    fun objToJson(any: Any?): String? {
        return try {
            gsonBuilder.create().toJson(any)
        } catch (e: Exception) {
            null
        }
    }

    fun <T> parseJson(json: String?, classOfT: Class<T>?): T? {
        if (json == null || classOfT == null) {
            return null
        }
        return try {
            gsonBuilder.create().fromJson(json, classOfT)
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }

    }
}