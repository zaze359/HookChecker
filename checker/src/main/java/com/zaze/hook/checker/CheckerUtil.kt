package com.zaze.hook.checker

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.Reader

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-19 - 13:28
 */
object CheckerUtil {

    @JvmStatic
    fun readByBytes(inputStream: InputStream, bufferSize: Int = 1024): StringBuilder {
        val results = StringBuilder()
        try {
            val bytes = ByteArray(bufferSize)
            var byteLength = inputStream.read(bytes)
            while (byteLength != -1) {
                results.append(String(bytes, 0, byteLength))
                byteLength = inputStream.read(bytes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return results
    }

    @JvmStatic
    fun readLine(reader: Reader): StringBuilder {
        var bfReader: BufferedReader? = null
        val results = StringBuilder()
        try {
            bfReader = BufferedReader(reader)
            var line = bfReader.readLine()
            while (line != null) {
                results.append(line)
                line = bfReader.readLine()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                bfReader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return results
    }
}