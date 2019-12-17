package com.zaze.hook.checker

import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-03 - 14:14
 */
object ExecUtil {

    fun exec(command: String): StringBuilder {
        val builder = StringBuilder()
        var process: Process? = null
        var os: DataOutputStream? = null
        try {
            process = Runtime.getRuntime().exec(command)
            os = DataOutputStream(process.outputStream)
            val inputStream = BufferedReader(InputStreamReader(process.inputStream))
            os.writeBytes("exit\n")
            os.flush()
            process.waitFor()
            var line = inputStream.readLine()
            while (line != null) {
                builder.append(line + "\n")
                line = inputStream.readLine()
            }
        } catch (e: Exception) {
            Log.e("CheckQemu", "exec error", e)
        } finally {
            try {
                os?.close()
            } catch (e: Exception) {
                // ignore
            }
            process?.destroy()
        }
        return builder
    }
}