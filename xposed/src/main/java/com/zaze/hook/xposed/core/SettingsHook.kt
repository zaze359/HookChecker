package com.zaze.hook.xposed.core

import android.provider.Settings
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-16 - 15:12
 */
class SettingsHook internal constructor(lpparam: XC_LoadPackage.LoadPackageParam) {
    companion object {
        const val TAG = "Settings"
    }

    val Secure = SecureHook(lpparam)

    class SecureHook(lpparam: XC_LoadPackage.LoadPackageParam) {
        private val hashMap = HashMap<String, String>()


        companion object {
            const val TAG = SettingsHook.TAG + ".Secure"
            val clazz = Settings.Secure::class.java
        }

        init {
            XposedBridge.hookAllMethods(
                clazz, "getString",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam?) {
                        if (param?.args == null || param.args.size <= 1) {
                            return
                        }
                        param.args.forEach {
                            XposedBridge.log("${BuildHook.TAG} $it")
                        }
                        XposedBridge.log("$TAG result ${param.result}")
                        if (hashMap.contains(param.args[1].toString())) {
                            param.result = hashMap[param.args[1].toString()]
                        }
                        XposedBridge.log("$TAG result hooked : ${param.result}")
                    }
                })
        }

        fun getString(name: String, result: String) {
            hashMap[name] = result
        }
    }
}