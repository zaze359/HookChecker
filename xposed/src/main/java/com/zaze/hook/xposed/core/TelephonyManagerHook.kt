package com.zaze.hook.xposed.core

import android.telephony.TelephonyManager
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-16 - 15:12
 */
class TelephonyManagerHook internal constructor(lpparam: XC_LoadPackage.LoadPackageParam) {
    companion object {
        const val TAG = "TelephonyManagerHook"
        val clazz = TelephonyManager::class.java
    }

    fun getDeviceId(result: String) {
        XposedHelpers.findAndHookMethod(clazz, "getDeviceId", XC_ResultHook(result))
    }
}