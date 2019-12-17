package com.zaze.hook.xposed.core

import com.zaze.hook.xposed.core.method.BuildHook
import com.zaze.hook.xposed.core.method.ContextHook
import com.zaze.hook.xposed.core.method.SettingsHook
import com.zaze.hook.xposed.core.method.TelephonyManagerHook
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
    val Context = ContextHook()
}