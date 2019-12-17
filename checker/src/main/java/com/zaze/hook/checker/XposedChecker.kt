package com.zaze.hook.checker

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-13 - 15:02
 */
internal object XposedChecker {
    fun detectXposed() {
        try {
            throw Exception("detect Xposed")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}