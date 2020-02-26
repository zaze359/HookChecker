package com.zaze.hook.xposed.core

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-16 - 15:33
 */
class XC_ResultHook(private val result: Any) : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam?) {
        if (param != null) {
            XposedBridge.log("${param.method.declaringClass.name} ${param.method.name} result: ${param.result}; hooked : ${result}")
            param.result = result
        }
    }
}