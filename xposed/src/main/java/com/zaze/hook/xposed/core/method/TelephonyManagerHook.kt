package com.zaze.hook.xposed.core.method

import android.telephony.TelephonyManager
import com.zaze.hook.xposed.core.XC_ResultHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-16 - 15:12
 */
class TelephonyManagerHook(lpparam: XC_LoadPackage.LoadPackageParam) {
    companion object {
        const val TAG = "TelephonyManagerHook"
        val clazz = TelephonyManager::class.java
    }

    fun getDeviceId(result: String) {
        XposedHelpers.findAndHookMethod(clazz, "getDeviceId", XC_ResultHook(result))
    }
}