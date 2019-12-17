package com.zaze.hook.xposed.core.method

import android.app.Application
import android.content.Context
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-17 - 14:40
 */
class ContextHook {
    private lateinit var context: Context

    init {
        XposedHelpers.findAndHookMethod(
            Application::class.java,
            "attach",
            Context::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    context = param.args[0] as Context
                }
            })

    }


    fun getContext(): Context {
        return context
    }
}