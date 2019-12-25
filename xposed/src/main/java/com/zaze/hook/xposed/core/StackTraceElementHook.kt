package com.zaze.hook.xposed.core

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-17 - 17:50
 */
class StackTraceElementHook internal constructor() {
    init {
        XposedHelpers.findAndHookMethod(
            StackTraceElement::class.java,
            "getClassName",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    param?.result?.toString()?.let {
                        if (it.contains("de.robv.android.xposed.")) {
                            param.result = "hooked"
                        }
                    }
                }
            })
    }
}