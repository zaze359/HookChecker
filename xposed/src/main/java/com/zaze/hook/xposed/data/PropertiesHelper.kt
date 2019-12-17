package com.zaze.hook.xposed.data

import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-13 - 16:24
 */
internal object PropertiesHelper {

    /**
     * 加载
     *
     * @param file
     * @return
     */
    fun load(file: File): Properties {
        val properties = Properties()
        if (file.exists()) {
            var inputStream: FileInputStream? = null
            try {
                inputStream = FileInputStream(file)
                properties.load(inputStream)
            } catch (e: Exception) {
                Log.e("DeviceInfoLoader", "load error", e)
            } finally {
                try {
                    inputStream?.close()
                } catch (e: Exception) {
                    // ignore
                }
            }
        }
        return properties
    }

    fun store(file: File, properties: Properties) {
        if (!file.exists()) {
            file.parentFile?.mkdirs()
            file.createNewFile()
        }

        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(file, false)
            properties.store(outputStream, "")
        } catch (e: Exception) {
            Log.e("DeviceInfoLoader", "store error", e)
        } finally {
            try {
                outputStream?.close()
            } catch (e: IOException) {
                // ignore
            }
        }
    }


}