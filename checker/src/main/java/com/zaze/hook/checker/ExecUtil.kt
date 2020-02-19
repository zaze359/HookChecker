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
    val SUCCESS = 0

    fun exec(commands: Array<String>): Pair<Int, StringBuilder> {
        var code = -1
        val builder = StringBuilder()
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
            code = process.waitFor()
            val inputStream = if (code == SUCCESS) {
                process.inputStream
            } else {
                process.errorStream
            }
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line = reader.readLine()
            while (line != null) {
                builder.append(line + "\n")
                line = reader.readLine()
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
        return Pair(code, builder)
    }
}