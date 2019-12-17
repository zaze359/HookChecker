package com.zaze.hook.xposed

import android.content.pm.ApplicationInfo
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-16 - 10:51
 */
object HookFilter {

    fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam): Boolean {
        if (lpparam.packageName == "android" || lpparam.packageName == "de.robv.android.xposed.installer") {
            // 过滤 部分应用
            return true
        }
        if (lpparam.appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
            return true
        }
        return false
    }
}