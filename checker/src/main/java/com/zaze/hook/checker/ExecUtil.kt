package com.zaze.hook.checker

import com.zaze.hook.checker.log.CheckerLog
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-03 - 14:14
 */
object ExecUtil {

    fun exec(commands: Array<String>): StringBuilder? {
        if (commands.isEmpty()) {
            return null
        }
        var builder: StringBuilder? = null
        var process: Process? = null
        var os: DataOutputStream? = null
        try {
            process = Runtime.getRuntime().exec(commands[0])
            os = DataOutputStream(process.outputStream)
            repeat(commands.size) {
                val command = commands[it]
                if (it != 0 && command.isNotEmpty() && command.isNotBlank()) {
                    os.write(command.toByteArray())
                    os.writeBytes("\n")
                }
            }
            os.writeBytes("exit\n")
            os.flush()
            process.waitFor()

            builder = CheckerUtil.readByBytes(process.inputStream)


            val inputStream = BufferedReader(InputStreamReader(process.inputStream))
            var line = inputStream.readLine()
            while (line != null) {
                if (builder == null) {
                    builder = StringBuilder()
                }
                builder.append(line + "\n")
                line = inputStream.readLine()
            }
        } catch (e: Exception) {
            CheckerLog.e("CheckQemu", "exec error", e)
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