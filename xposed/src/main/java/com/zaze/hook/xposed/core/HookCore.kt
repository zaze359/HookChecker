package com.zaze.hook.xposed.core

import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-16 - 15:07
 */
class HookCore(lpparam: XC_LoadPackage.LoadPackageParam) {
    val TelephonyManager = TelephonyManagerHook(lpparam)
    val Settings = SettingsHook(lpparam)
    val Build = BuildHook(lpparam)
    val Application = ApplicationHook()
    val StackTraceElement = StackTraceElementHook()
}