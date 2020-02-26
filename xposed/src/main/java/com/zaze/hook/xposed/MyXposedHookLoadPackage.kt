package com.zaze.hook.xposed

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2020-02-09 - 21:10
 */
abstract class MyXposedHookLoadPackage : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (HookFilter.handleLoadPackage(lpparam)) {
            return
        }
        try {
            actualHandleLoadPackage(lpparam)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    abstract fun actualHandleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam)

}