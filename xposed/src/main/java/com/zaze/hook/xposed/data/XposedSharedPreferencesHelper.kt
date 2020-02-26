package com.zaze.hook.xposed.data

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import com.zaze.hook.xposed.Utils
import com.zaze.hook.xposed.devices.DeviceInfo

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-17 - 13:30
 */
object XposedSharedPreferencesHelper {

    /**
     *
     */
    fun getSharePreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("xposed_sp", Context.MODE_PRIVATE)
    }

//    fun getCustomDeviceInfo(context: Context): DeviceInfo? {
//        return Utils.parseJson(
//            getSharePreferences(context).getString("deviceInfo", "{}"), DeviceInfo::class.java
//        )
//    }

    fun getCustomDeviceInfo(context: Context): DeviceInfo? {
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(
                Uri.parse(HookProvider.URI_DEVICE_INFO),
                null,
                null,
                null,
                null
            )
            if (cursor != null && cursor.moveToNext()) {
                return Utils.parseJson(
                    cursor.getString(cursor.getColumnIndex("customDeviceInfo")),
                    DeviceInfo::class.java
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return null
    }

    fun saveCustomDeviceInfo(context: Context, deviceInfo: DeviceInfo?) {
        getSharePreferences(context).edit().putString("customDeviceInfo", Utils.objToJson(deviceInfo))
            .apply()
    }
}