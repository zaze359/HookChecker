package com.zaze.hook.checker

import java.io.File

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-19 - 13:46
 */
class RootChecker {

    companion object {
        const val TAG = "RootChecker"
    }

    val result = CheckResult()

    fun detectRoot() {
        detectByFile()
        detectByCmd()
    }

    private fun detectByCmd() {
        val cmdResult = ExecUtil.exec(arrayOf("su"))
        if (cmdResult.first == ExecUtil.SUCCESS) {
            result.addError("$TAG hit detectByCmd: su ${cmdResult.second} ")

        }
//        return !TextUtils.isEmpty().toString())
    }

    private fun detectByFile() {
        arrayOf(
            "/system/bin/",
            "/system/xbin/",
            "/system/sbin/",
            "/sbin/",
            "/vendor/bin/"
        ).forEach {
            if (File("${it}su").exists()) {
                result.addError("$TAG hit detectByFile: ${it}su")
            }
        }
    }
}