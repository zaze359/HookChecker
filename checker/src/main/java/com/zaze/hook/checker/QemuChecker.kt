package com.zaze.hook.checker

import android.os.Build
import com.zaze.hook.checker.log.CheckerLog

/**
 * Description : 检测模拟器
 * @author : ZAZE
 * @version : 2019-12-02 - 18:54
 */
class QemuChecker {
    companion object {
        private const val TAG = "QemuChecker"
    }

    val result = CheckResult()
    val propMap = HashMap<String, String>()

//    val pattern = Pattern.compile("[.*]:[.*]\n")

    fun detectEmulator() {
        propMap.clear()
        propMap.putAll(getAllProp())
        detectEmulatorByProp(propMap)
        checkFingerprintMatched(propMap)
    }

    /**
     * 通过Prop 判断是否在模拟器中
     * @return true 在模拟器中运行
     */
    private fun detectEmulatorByProp(propMap: HashMap<String, String>) {
        val matchKv = { key: String, value: String? ->
            when (key) {
                "ro.kernel.qemu" -> {
                    // 模拟器中为1，通常在正常手机中没有该属性
                    "1" == value
                }
                "ro.build.tags" -> {
                    // 部分模拟器中为test-keys，通常在正常手机中它的值为release-keys
                    "release-keys" != value
                }
                "ro.product.model" -> {
                    // 模拟器中为sdk，通常在正常手机中它的值为手机的型号
                    value?.let {
                        it.contains("Android SDK") || (it == "sdk")
                    } ?: false
                }
                "ro.product.cpu.abi" -> {
                    // x86架构在真机中极少，后续若出现单独适配
                    "x86" == value
                }
                else -> {
                    false
                }
            }
        }
        propMap.keys.forEach { key ->
            val value = propMap[key]
            if (matchKv(key, value)) {
                result.addError("$TAG hit detectEmulatorByProp $key: $value")
            }
        }
    }

    /**
     * 检测fingerpring是否能正常匹配
     * [samsung/wisdomwifizc/wisdomwifi:9/PPR1.180610.011/P200ZCU2ASJ3_B2BF:user/release-keys]
     * getString("ro.product.brand") + '/' +
     * getString("ro.product.name") + '/' +
     * getString("ro.product.device") + ':' + getString("ro.build.version.release") + '/' +
     * getString("ro.build.id") + '/' +
     * getString("ro.build.version.incremental") + ':' + getString("ro.build.type") + '/'  +
     * getString("ro.build.tags");
     */
    private fun checkFingerprintMatched(propMap: HashMap<String, String>) {
        CheckerLog.d(TAG, "Build.FINGERPRINT : ${Build.FINGERPRINT}")
        val propFingerPrint = propMap["ro.build.fingerprint"]
        CheckerLog.d(TAG, "ro.build.fingerprint : $propFingerPrint")
        val isMatched = propFingerPrint?.split("/")?.let {
            it.contains(propMap["ro.product.brand"] ?: "")
                    && it.contains(propMap["ro.product.name"] ?: "")
                    && it.contains("${propMap["ro.product.device"]}:${propMap["ro.build.version.release"]}")
                    && it.contains(propMap["ro.build.id"] ?: "")
//                    && it.contains("${propMap["ro.build.version.incremental"]}:${propMap["ro.build.type"]}") // 定制存在 xxx.DM
                    && it.contains(propMap["ro.build.tags"] ?: "")
        } ?: false
        if (!isMatched) {
            result.addError("$TAG hit checkFingerprintMatched false: $propFingerPrint")
        }
//        val roBootimageBuildFingerprint = propMap["ro.bootimage.build.fingerprint"]
//        if (TextUtils.isEmpty(roBootimageBuildFingerprint) || TextUtils.isEmpty(roBuildFingerprint)) {
//            return false
//        }
//        if (Build.VERSION.SDK_//            return roBootimageBuildFingerprint == roBuildFingerprintINT >= Build.VERSION_CODES.O) {
//        } else {
//        }
    }

    fun getAllProp(): HashMap<String, String> {
        val propMap = HashMap<String, String>()
        ExecUtil.exec(arrayOf("getprop")).second.toString()
            .replace("[", "")
            .replace("]", "")
            .split("\n").forEach {
                val kv = it.split(": ")
//                CheckerLog.d(TAG, "kv : $it")
                if (kv.size >= 2) {
                    propMap[kv[0]] = kv[1]
                }
            }
        return propMap
    }
}