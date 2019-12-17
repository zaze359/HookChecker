package com.zaze.hook.checker

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Debug
import android.util.Log

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-11-27 - 14:07
 */
internal object DebuggerChecker {
    private const val TAG = "DebuggerChecker"
    fun detectDebugger(context: Context) {
        Log.i(
            TAG,
            "FLAG_DEBUGGABLE : ${context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE}"
        )
        Log.i(TAG, "isDebuggerConnected : ${Debug.isDebuggerConnected()}")
    }
}