package com.zaze.hook.xposed

import android.content.pm.ApplicationInfo
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-16 - 10:51
 */
object HookFilter {

    val pkgSet = setOf("android", "com.topjohnwu.magisk", "de.robv.android.xposed.installer")

    fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam): Boolean {
        if (pkgSet.contains(lpparam.packageName)) {
            XposedBridge.log("过滤重要应用不hook: ${lpparam.packageName}")
            return true
        }
        if (lpparam.appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
            return true
        }
        return false
    }
}