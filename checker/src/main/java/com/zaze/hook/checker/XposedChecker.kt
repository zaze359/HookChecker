package com.zaze.hook.checker

import android.os.Process
import com.zaze.hook.checker.log.CheckerLog
import java.io.FileInputStream
import java.util.HashSet

/**
 * Description : 检测 Xposed
 * @author : ZAZE
 * @version : 2019-12-13 - 15:02
 */
class XposedChecker {
    companion object {
        const val TAG = "XposedChecker"
    }

    val result = CheckResult()

    /**
     * 检测能否构建XposedHelpers类
     * 正常设备应该抛出ClassNotFoundException
     */
    fun detectByClassLoader() {
        try {
            ClassLoader.getSystemClassLoader()
                .loadClass("de.robv.android.xposed.XposedHelpers")
                .newInstance() != null
        } catch (e: Exception) {
            CheckerLog.e(TAG, "detectByClassLoader flag ", e)
            if (e !is ClassNotFoundException) {
                result.addError("$TAG hit detectByClassLoader : $e")
            }
        }
    }

    /**
     * 1. 检测栈信息包含xposed包名
     * 2. 检测栈信息中ZygoteInit, 一般最底层栈为 com.android.internal.os.ZygoteInit.main
     * 需要在主线中调用
     */
    fun detectByStackTrace() {
        if (Thread.currentThread().id == Process.myPid().toLong()) {
            throw IllegalThreadStateException("必须在主线程调用")
        }
        val stackTrace = Throwable("detect Xposed").apply {
            this.printStackTrace()
        }.stackTrace
        var hasZygote = false
        var bottomStackIsZygote = false
        var flag = 0
        repeat(stackTrace.count()) {
            val stackStr = stackTrace[it].toString()
            if (stackStr.contains("de.robv.android.xposed")) {
                flag = flag or 1
            }
            if (stackStr.contains("com.android.internal.os.ZygoteInit")) {
                hasZygote = true
                if (stackTrace.size - 1 == it) {
                    bottomStackIsZygote = true
                }
            }
        }
        CheckerLog.e(TAG, "hasXposed flag : $flag; hasZygote: $hasZygote; bottomStackIsZygote: ${bottomStackIsZygote}")
        result.addMessageNoError("$TAG detectByStackTrace: ${stackTrace.toList()}")
        when {
            flag > 0 -> {
                result.addError("$TAG hit detectByStackTrace: 包含了xposed的栈信息 $flag")
            }
            !bottomStackIsZygote -> {
                result.addError("$TAG hit detectByStackTrace: 最底下栈没有包含zygote信息 $bottomStackIsZygote")
            }
            !hasZygote -> {
                result.addError("$TAG hit detectByStackTrace: 没有Zygote栈信息")
            }
            else -> {
                // safe

            }
        }
    }

    /**
     * 去读maps检测是否加载类xposed 的so 和jar
     */
    fun detectByMaps() {
        val hashSet = HashSet<String>()
        val buffer = CheckerUtil.readByBytes(FileInputStream("/proc/${Process.myPid()}/maps"))
        buffer.toString().split("\n").forEach { line ->
            if ((line.endsWith(".so") || line.endsWith(".jar"))
                && line.contains("xposed", true)
            ) {
                hashSet.add(line)
                CheckerLog.d(TAG, "line $line")
            }
        }
        if (hashSet.isNotEmpty()) {
            CheckerLog.e(TAG, "has Xposed lib ${hashSet.size}")
            result.addError("$TAG hit detectByMaps: 加载了 xposed 的so 和jar")
        }
    }
}