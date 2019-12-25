package com.zaze.hook.checker

import android.text.TextUtils
import java.io.File

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-19 - 13:46
 */
object RootChecker {

    fun detectRoot(): Boolean {
        return detectByFile() || detectByCmd()
    }

    private fun detectByCmd(): Boolean {
        return !TextUtils.isEmpty(ExecUtil.exec(arrayOf("su", "/system/xbin/which")).toString())
    }

    private fun detectByFile(): Boolean {
        arrayOf(
            "/system/bin/",
            "/system/xbin/",
            "/system/sbin/",
            "/sbin/",
            "/vendor/bin/"
        ).forEach {
            if (File("${it}su").exists()) {
                return true
            }
        }
        return false
    }


}