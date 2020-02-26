package com.zaze.hook.xposed.devices

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.zaze.hook.xposed.data.XposedSharedPreferencesHelper
import java.util.*


/**
 * Description :
 * @author : ZAZE
 * @version : 2017-08-01 - 13:43
 */
@SuppressLint("HardwareIds")
object DeviceUtil {
    /**
     * 设备第一次启动时产生和存储的64bit的一个数，当设备被wipe后该数重置
     */
    @JvmStatic
    fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    @SuppressLint("MissingPermission")
    @JvmStatic
    fun getDeviceId(context: Context): String? {
        return getTelephonyManager(context).deviceId
    }

    @SuppressLint("MissingPermission")
    @JvmStatic
    fun getSimSerialNumber(context: Context): String? {
        return getTelephonyManager(context).simSerialNumber
    }

    @JvmStatic
    fun getTelephonyManager(context: Context): TelephonyManager {
        return context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    /**
     * SimSerialNumber -> DeviceId(IMEI) -> AndroidId -> randomId
     * [context] context
     */
    @JvmStatic
    fun getUUID(context: Context): String {
        val key = "getUUID"
        var id = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Build.getSerial()
        } else {
            Build.SERIAL
        }
        if (TextUtils.isEmpty(id)) {
            id = getSimSerialNumber(context)
            if (TextUtils.isEmpty(id)) {
                id = getDeviceId(context)
                if (TextUtils.isEmpty(id)) {
                    id = getAndroidId(context)
                    if ("9774d56d682e549c" == id) {
                        val sp = XposedSharedPreferencesHelper.getSharePreferences(context)
                        id = sp.getString(key, "")
                        if (TextUtils.isEmpty(id)) {
                            id = UUID.randomUUID().toString()
                            sp.edit().putString(key, id).apply()
                        }
                    }
                }
            }
        }
        return id
    }

    /**
     * Runtime Max Memory
     * 单个应用 最大运存
     * @return
     */
    @JvmStatic
    fun getRuntimeMaxMemory(): Long {
        return Runtime.getRuntime().maxMemory()
    }

    /**
     * Runtime Free Memory
     * 当前 从机器内存中取过来的 内存的 中的空闲内存
     * @return
     */
    @JvmStatic
    fun getRuntimeFreeMemory(): Long {
        return Runtime.getRuntime().freeMemory()
    }

    /**
     * Runtime Total Memory
     * 当前 从机器内存中取过来 的 总内存(包括使用了的和 freeMemory)
     * @return
     */
    @JvmStatic
    fun getRuntimeTotalMemory(): Long {
        return Runtime.getRuntime().totalMemory()
    }

    // --------------------------------------------------
    // --------------------------------------------------

    @JvmStatic
    fun getDeviceMemory(context: Context): ActivityManager.MemoryInfo {
        val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val outInfo = ActivityManager.MemoryInfo()
        am.getMemoryInfo(outInfo)
        return outInfo
    }

}
