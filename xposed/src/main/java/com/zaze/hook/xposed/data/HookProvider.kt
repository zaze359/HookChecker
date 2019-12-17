package com.zaze.hook.xposed.data

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import java.io.File

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-16 - 18:29
 */
class HookProvider : ContentProvider() {
    companion object {
        val AUTHORITY = "com.zaze.hook.xposed.provider"
        val URI_DEVICE_INFO = "content://$AUTHORITY/device/info"
        val deviceProperties = PropertiesHelper.load(File("/sdcard/zaze/deviceInfo"))
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        context?.let {
            val map = XposedSharedPreferencesHelper.getSharePreferences(it).all
            if (map == null || map.isEmpty()) {
                return null
            }
            val cursor = MatrixCursor(arrayOf("key", "value"))
            map.forEach { entry ->
                cursor.addRow(arrayOf(entry.key, entry.value.toString()))
            }
            return cursor
        }
        return null
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

}