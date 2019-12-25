package com.zaze.hook.xposed.core

import android.app.Application
import android.content.Context
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-17 - 14:40
 */
class ApplicationHook internal constructor() {

    fun getContext(methodHook: XC_MethodHook) {
        XposedHelpers.findAndHookMethod(
            Application::class.java,
            "attach",
            Context::class.java,
            methodHook
        )
    }
}