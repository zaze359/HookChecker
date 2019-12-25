package com.zaze.hook.checker

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Debug

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-11-27 - 14:07
 */
object DebuggerChecker {
    private const val TAG = "DebuggerChecker"
    fun detectDebugger(context: Context): Boolean {
        return context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE > 0 || Debug.isDebuggerConnected()
//        CheckerLog.i(
//            TAG,
//            "FLAG_DEBUGGABLE : ${}"
//        )
//        CheckerLog.i(TAG, "isDebuggerConnected : ${Debug.isDebuggerConnected()}")
    }
}