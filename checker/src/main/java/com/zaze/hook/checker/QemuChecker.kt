package com.zaze.hook.checker

import android.content.Context
import android.os.Build
import com.zaze.hook.checker.log.CheckerLog

/**
 * Description : 检测模拟器
 * @author : ZAZE
 * @version : 2019-12-02 - 18:54
 */
object QemuChecker {
    private const val TAG = "QemuChecker"

//    val pattern = Pattern.compile("[.*]:[.*]\n")
    /**
     * 通过Prop 判断是否在模拟器中
     * @return true 在模拟器中运行
     */
    fun detectEmulatorByProp(context: Context): Boolean {
        val propMap = getAllProp()
        // 模拟器中为1，通常在正常手机中没有该属性
        val isQemuKernel = propMap["ro.kernel.qemu"] == "1"
        // 部分模拟器中为test-keys，通常在正常手机中它的值为release-keys
        val isDebugTags = propMap["ro.build.tags"] != "release-keys"
        // 模拟器中为sdk，通常在正常手机中它的值为手机的型号
        val isQemuModel = propMap["ro.product.model"]?.let {
            it.contains("Android SDK") || (it == "sdk")
        } ?: true
        // x86架构在真机中极少，后续若出现单独适配
        val isX86 = propMap["ro.product.cpu.abi"] == "x86"
        CheckerLog.e(TAG, "isQemuKernel : $isQemuKernel")
        CheckerLog.e(TAG, "isDebugTags : $isDebugTags")
        CheckerLog.e(TAG, "isX86 : $isX86")
        CheckerLog.e(TAG, "isQemuModel : $isQemuModel")
        CheckerLog.e(TAG, "checkFingerprintMatched : ${checkFingerprintMatched(propMap)}")
        return isX86 || isQemuKernel || isDebugTags || isQemuModel || !checkFingerprintMatched(
            propMap
        )
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
    private fun checkFingerprintMatched(propMap: HashMap<String, String>): Boolean {
        CheckerLog.d(TAG, "Build.FINGERPRINT : ${Build.FINGERPRINT}")
        CheckerLog.d(TAG, "ro.build.fingerprint : ${propMap["ro.build.fingerprint"]}")
        return propMap["ro.build.fingerprint"]?.split("/")?.let {
            it.contains(propMap["ro.product.brand"] ?: "")
                    && it.contains(propMap["ro.product.name"] ?: "")
                    && it.contains("${propMap["ro.product.device"]}:${propMap["ro.build.version.release"]}")
                    && it.contains(propMap["ro.build.id"] ?: "")
//                    && it.contains("${propMap["ro.build.version.incremental"]}:${propMap["ro.build.type"]}") // 定制存在 xxx.DM
                    && it.contains(propMap["ro.build.tags"] ?: "")
        } ?: false


//        val roBootimageBuildFingerprint = propMap["ro.bootimage.build.fingerprint"]
//        if (TextUtils.isEmpty(roBootimageBuildFingerprint) || TextUtils.isEmpty(roBuildFingerprint)) {
//            return false
//        }
//        if (Build.VERSION.SDK_//            return roBootimageBuildFingerprint == roBuildFingerprintINT >= Build.VERSION_CODES.O) {
//        } else {
//        }
    }

    private fun getAllProp(): HashMap<String, String> {
        val propMap = HashMap<String, String>()
        ExecUtil.exec(arrayOf("getprop"))?.toString()
            ?.replace("[", "")
            ?.replace("]", "")
            ?.split("\n")?.forEach {
                val kv = it.split(": ")
//                CheckerLog.d(TAG, "kv : $it")
                if (kv.size >= 2) {
                    propMap[kv[0]] = kv[1]
                }
            }
        return propMap
    }
}