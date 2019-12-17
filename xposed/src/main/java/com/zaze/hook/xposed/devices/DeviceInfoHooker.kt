package com.zaze.hook.xposed.devices

import android.content.Context
import android.provider.Settings
import com.zaze.hook.xposed.HookFilter
import com.zaze.hook.xposed.core.HookCore
import com.zaze.hook.xposed.data.XposedSharedPreferencesHelper
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-13 - 16:21
 */
class DeviceInfoHooker : IXposedHookLoadPackage {

    companion object {
        private const val TAG = "HookDeviceInfo"
    }

    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (HookFilter.handleLoadPackage(lpparam)) {
            return
        }
        val hookCore = HookCore(lpparam)

        hookCore.Application.getApplication(object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                XposedBridge.log("$TAG application : ${param.thisObject}")
                val context = param.args[0] as Context
                XposedBridge.log("$TAG context : $context")
                val deviceInfo = XposedSharedPreferencesHelper.getDeviceInfo(context!!)
                deviceInfo?.deviceId?.let {
                    XposedBridge.log("$TAG getDeviceId : $it")
                    hookCore.TelephonyManager.getDeviceId(it)
                    hookCore.Build.SERIAL(it)
                    hookCore.Build.getSerial(it)
                    hookCore.Settings.Secure.getString(Settings.Secure.ANDROID_ID, it)
                }
                deviceInfo?.deviceModel?.let {
                    XposedBridge.log("$TAG getDeviceModel : $it")
                    hookCore.Build.MODEL(it)
                    hookCore.Build.DEVICE(it)
                    hookCore.Build.HARDWARE(it)
                }
            }
        })
    }
}