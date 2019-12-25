package com.zaze.hook.checker.log

import android.util.Log

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-04-02 - 16:08
 */
object CheckerLog {

    private var logClient: CheckerLogClient? = null

    private fun getLogClient(): CheckerLogClient? {
        return logClient
    }

    @JvmStatic
    fun setLogClient(logClient: CheckerLogClient) {
        CheckerLog.logClient = logClient
    }

    @JvmStatic
    fun v(tag: String, msg: String) {
        getLogClient()?.v(tag, msg) ?: Log.v(tag, msg)
    }

    @JvmStatic
    fun d(tag: String, msg: String) {
        getLogClient()?.d(tag, msg) ?: Log.d(tag, msg)
    }

    @JvmStatic
    fun i(tag: String, msg: String) {
        getLogClient()?.i(tag, msg) ?: Log.i(tag, msg)
    }

    @JvmStatic
    fun w(tag: String, msg: String) {
        getLogClient()?.w(tag, msg) ?: Log.w(tag, msg)
    }

    @JvmStatic
    fun w(tag: String, msg: String, tr: Throwable) {
        getLogClient()?.w(tag, msg, tr) ?: Log.w(tag, msg, tr)
    }

    @JvmStatic
    fun e(tag: String, msg: String) {
        getLogClient()?.e(tag, msg) ?: Log.e(tag, msg)
    }

    @JvmStatic
    fun e(tag: String, msg: String, tr: Throwable) {
        getLogClient()?.e(tag, msg, tr) ?: Log.e(tag, msg, tr)
    }

    @JvmStatic
    fun log(tag: String, msg: String, isImportant: Boolean? = null) {
        when {
            isImportant == null -> v(tag, msg)
            isImportant -> e(tag, msg)
            else -> d(tag, msg)
        }

    }

}