package com.zaze.hook.checker

import android.os.Process
import com.zaze.hook.checker.log.CheckerLog
import java.io.FileInputStream

/**
 * Description : 检测 Xposed
 * @author : ZAZE
 * @version : 2019-12-13 - 15:02
 */
object XposedChecker {
    const val TAG = "XposedChecker"
    /**
     * 检测能否构建XposedHelpers类
     * 正常设备应该抛出ClassNotFoundException
     */
    fun detectByClassLoader(): Boolean {
        return try {
            ClassLoader.getSystemClassLoader().loadClass("de.robv.android.xposed.XposedHelpers")
                .newInstance() != null
        } catch (e: Exception) {
            CheckerLog.e(TAG, "detectByClassLoader flag ", e)
            if (e !is ClassNotFoundException) {
                return true
            }
            false
        }
    }

    /**
     * 1. 检测栈信息包含xposed包名
     * 2. 检测栈信息中ZygoteInit, 一般最底层栈为 com.android.internal.os.ZygoteInit.main
     *
     * @return true if had xposed framework
     */
    fun detectByStackTrace(): Boolean {
        if (Thread.currentThread().id == Process.myPid().toLong()) {
            throw IllegalThreadStateException("必须在主线程调用")
        }
        val stackTrace = Throwable("detect Xposed").stackTrace
        var hasZygote = false
        var flag = 0
        repeat(stackTrace.count()) {
            val stackStr = stackTrace[it].toString()
//            CheckerLog.v("XposedChecker $it", stackStr)
            if (stackStr.contains("de.robv.android.xposed")) {
                flag = flag or 1
            }
            if (stackStr.contains("com.android.internal.os.ZygoteInit")) {
                hasZygote = true
                if (stackTrace.size - 1 != it) {
                    flag = flag or (1 shl 1)
                }
            }
        }
        CheckerLog.e(TAG, "hasXposed flag : $flag; hasZygote: $hasZygote")
        return flag > 0 || !hasZygote
    }

    /**
     * 去读maps检测是否加载类xposed 的so 和jar
     */
    fun detectByMaps(): Boolean {
        val hashSet = HashSet<String>()
        val buffer = CheckerUtil.readByBytes(FileInputStream("/proc/${Process.myPid()}/maps"))
        buffer.toString().split("\n").forEach { line ->
            if ((line.endsWith(".so") || line.endsWith(".jar")) && line.contains(
                    "xposed",
                    true
                )
            ) {
                hashSet.add(line)
                CheckerLog.d(TAG, "line $line")
            }
        }
        CheckerLog.e(TAG, "has Xposed lib ${hashSet.size}")
        return hashSet.isNotEmpty()
    }
//    /**
//     * 去读maps检测是否加载类xposed 的so 和jar
//     */
//    fun detectByMaps(): Boolean {
//        var bufferedReader: BufferedReader? = null
//        val hashSet = HashSet<String>()
//        try {
//            bufferedReader = BufferedReader(
//                InputStreamReader(
//                    FileInputStream("/proc/${Process.myPid()}/maps"),
//                    "utf-8"
//                )
//            )
//            var line = bufferedReader.readLine()
//            while (line != null) {
//                if ((line.endsWith(".so") || line.endsWith(".jar")) && line.contains(
//                        "xposed",
//                        true
//                    )
//                ) {
//                    hashSet.add(line)
//                    CheckerLog.d("XposedChecker line", line)
//                }
//                line = bufferedReader.readLine()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            bufferedReader?.close()
//        }
//        CheckerLog.e(TAG, "has Xposed lib ${hashSet.size}")
//        return hashSet.isNotEmpty()
//    }
}