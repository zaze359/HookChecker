package com.zaze.hook.checker

import com.zaze.common.thread.ThreadPlugins
import com.zaze.hook.checker.log.CheckerLog
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-11-27 - 13:54
 */
object DeviceChecker {

    private const val TAG = "DeviceChecker"


    /**
     * 检测安全
     */
    fun detectSafely() {
        ThreadPlugins.runInWorkThread(Runnable {
            val messageBuilder = StringBuilder()
            val xposedChecker = XposedChecker().apply {
                detectByClassLoader()
                detectByMaps()
            }
            val qemuChecker = QemuChecker().apply {
                detectEmulator()
            }
            val rootChecker = RootChecker().apply {
                detectRoot()
            }

            messageBuilder.append(qemuChecker.result.messageBuilder)
            messageBuilder.append(rootChecker.result.messageBuilder)
            messageBuilder.append(xposedChecker.result.messageBuilder)
            val countDownLatch = CountDownLatch(1)
            ThreadPlugins.runInUIThread(Runnable {
                xposedChecker.detectByStackTrace()
                countDownLatch.countDown()
            })
            countDownLatch.await(5000L, TimeUnit.SECONDS)
            log("Emulator", qemuChecker.result.isError())
            log("Root", rootChecker.result.isError())
            log("Xposed", xposedChecker.result.isError())
            CheckerLog.log(TAG, messageBuilder.toString(), true)
//            CheckerLog.log(TAG, "All Prop ${qemuChecker.getAllProp()}", true)
        })

    }


    fun log(tag: String, bool: Boolean) {
        CheckerLog.log(TAG, "$tag : $bool", bool)
    }
}