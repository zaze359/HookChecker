package com.zaze.hook.checker

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-02 - 18:54
 */
internal object QemuChecker {
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
        Log.e(TAG, "isQemuKernel : $isQemuKernel")
        Log.e(TAG, "isDebugTags : $isDebugTags")
        Log.e(TAG, "isX86 : $isX86")
        Log.e(TAG, "isQemuModel : $isQemuModel")
        Log.e(TAG, "checkFingerprintMatched : ${checkFingerprintMatched(propMap)}")
        return if (isX86 || isQemuKernel || isDebugTags || isQemuModel || !checkFingerprintMatched(
                propMap
            )
        ) {
            Log.e(TAG, "程序运行在模拟器中!")
            Toast.makeText(context, "程序运行在模拟器中!", Toast.LENGTH_SHORT).show()
            true
        } else {
            Log.i(TAG, "程序运行在真实设备中!")
            Toast.makeText(context, "程序运行在真实设备中!", Toast.LENGTH_SHORT).show()
            false
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
    private fun checkFingerprintMatched(propMap: HashMap<String, String>): Boolean {
        Log.d(TAG, "Build.FINGERPRINT : ${Build.FINGERPRINT}")
        Log.d(TAG, "ro.build.fingerprint : ${propMap["ro.build.fingerprint"]}")
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
        ExecUtil.exec("getprop").toString()
            .replace("[", "")
            .replace("]", "")
            .split("\n").forEach {
                val kv = it.split(": ")
                Log.d(TAG, "kv : $it")
                if (kv.size >= 2) {
                    propMap[kv[0]] = kv[1]
                }
            }
        return propMap
    }
}