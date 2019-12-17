package com.zaze.hook.xposed.core.method

import android.os.Build
import com.zaze.hook.xposed.core.XC_ResultHook
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-16 - 15:12
 */
class BuildHook(lpparam: XC_LoadPackage.LoadPackageParam) {
    private val buildMap = HashMap<String, String>()

    companion object {
        const val TAG = "Build"
    }

    init {
        XposedBridge.hookAllMethods(
            XposedHelpers.findClass(
                "android.os.SystemProperties",
                lpparam.classLoader
            ), "get", object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    if (param?.args == null || param.args.isEmpty()) {
                        return
                    }
                    param.args?.forEach {
                        XposedBridge.log("$TAG $it")
                    }
                    XposedBridge.log("$TAG result $${param.result}")
                    val stringArg = param.args?.get(0)?.toString() ?: ""
                    if (buildMap.contains(stringArg)) {
                        param.result = buildMap[stringArg]
                    }
                    XposedBridge.log("$TAG result hooked : ${param.result}")
                }
            })
    }


    fun MANUFACTURER(value: String) {
        XposedHelpers.setStaticObjectField(android.os.Build::class.java, "MANUFACTURER", value)
        buildMap["ro.product.manufacturer"] = value
    }

    fun BRAND(value: String) {
        XposedHelpers.setStaticObjectField(android.os.Build::class.java, "BRAND", value)
        buildMap["ro.product.brand"] = value
    }

    fun BOOTLOADER(value: String) {
        XposedHelpers.setStaticObjectField(android.os.Build::class.java, "BOOTLOADER", value)
        buildMap["ro.bootloader"] = value
    }

    fun MODEL(value: String) {
        XposedHelpers.setStaticObjectField(android.os.Build::class.java, "MODEL", value)
        buildMap["ro.product.model"] = value
    }

    fun DEVICE(value: String) {
        XposedHelpers.setStaticObjectField(android.os.Build::class.java, "DEVICE", value)
        buildMap["ro.product.device"] = value
    }

    fun DISPLAY(value: String) {
        XposedHelpers.setStaticObjectField(android.os.Build::class.java, "DISPLAY", value)
        buildMap["ro.build.display.id"] = value
    }

    fun PRODUCT(value: String) {
        XposedHelpers.setStaticObjectField(android.os.Build::class.java, "PRODUCT", value)
        buildMap["ro.product.name"] = value
    }

    fun BOARD(value: String) {
        XposedHelpers.setStaticObjectField(android.os.Build::class.java, "BOARD", value)
        buildMap["ro.product.board"] = value
    }

    fun HARDWARE(value: String) {
        XposedHelpers.setStaticObjectField(android.os.Build::class.java, "HARDWARE", value)
        buildMap["ro.hardware"] = value
    }

    fun SERIAL(value: String) {
        XposedHelpers.setStaticObjectField(android.os.Build::class.java, "SERIAL", value)
        buildMap["ro.serialno"] = value
        buildMap["no.such.thing"] = value
    }

    fun getSerial(value: String) {
        XposedHelpers.findAndHookMethod(
            Build::class.java,
            "getSerial", XC_ResultHook(value)
        )
    }

    fun TYPE(value: String) {
        XposedHelpers.setStaticObjectField(android.os.Build::class.java, "TYPE", value)
        buildMap["ro.build.type"] = value
    }

    fun TAGS(value: String) {
        XposedHelpers.setStaticObjectField(android.os.Build::class.java, "TAGS", value)
        buildMap["ro.build.tags"] = value
    }

    fun FINGERPRINT(value: String) {
        XposedHelpers.setStaticObjectField(android.os.Build::class.java, "FINGERPRINT", value)
        buildMap["ro.build.fingerprint"] = value
    }

    fun USER(value: String) {
        XposedHelpers.setStaticObjectField(android.os.Build::class.java, "USER", value)
        buildMap["ro.build.user"] = value
    }

    fun HOST(value: String) {
        XposedHelpers.setStaticObjectField(android.os.Build::class.java, "HOST", value)
        buildMap["ro.build.host"] = value
    }
}