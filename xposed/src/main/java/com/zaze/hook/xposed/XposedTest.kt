package com.zaze.hook.xposed

import android.content.Context
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-12-13 - 13:21
 */
class XposedTest : IXposedHookLoadPackage {

    companion object {
        private val TAG = "XposedTest"
    }

    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
//        XposedBridge.log("$TAG >> handleLoadPackage : ${lpparam.packageName}")
        if (HookFilter.handleLoadPackage(lpparam)) {
            return
        }
//        HookCore(lpparam)
        if ("com.zaze.demo" == lpparam.packageName) {
            val testDebugClass = lpparam.classLoader.loadClass("com.zaze.demo.debug.TestDebug")
            XposedHelpers.findAndHookMethod(
                testDebugClass,
                "test",
                Context::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam?) {
                        super.afterHookedMethod(param)
                        XposedBridge.log("$TAG >> afterHookedMethod ${param?.args?.get(0)}")

                    }

                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        super.beforeHookedMethod(param)
                        XposedBridge.log("$TAG >> beforeHookedMethod ${param?.args?.get(0)}")
                    }
                })

            val entity = lpparam.classLoader.loadClass("com.zaze.demo.model.entity.BaseEntity")
            XposedHelpers.findAndHookMethod(entity, "getName", object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    XposedBridge.log("$TAG >> beforeHookedMethod ${param?.thisObject} >> ${param?.result}")
                    param?.result = "Hooked ${param?.result}"
                }
            })
        }
    }
}
