package com.zaze.hook.checker

import android.content.Context
import com.zaze.common.thread.ThreadPlugins
import com.zaze.hook.checker.log.CheckerLog

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
    fun detectSafely(context: Context) {
        val messageBuilder = StringBuilder()
        //
        val xposedChecker = XposedChecker().apply {
            this.detectByStackTrace()
        }

        ThreadPlugins.runInWorkThread(Runnable {
            if (!xposedChecker.result.isError()) {
                xposedChecker.detectByClassLoader()
                xposedChecker.detectByMaps()
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

            log("Emulator", qemuChecker.result.isError())
            log("Root", rootChecker.result.isError())
            log("Xposed", xposedChecker.result.isError())

            CheckerLog.log(TAG, "messageBuilder $messageBuilder", true)
            CheckerLog.log(TAG, "All Prop ${qemuChecker.getAllProp()}", true)
        })
    }


    fun log(tag: String, bool: Boolean) {
        CheckerLog.log(TAG, "$tag : $bool", bool)
    }
}