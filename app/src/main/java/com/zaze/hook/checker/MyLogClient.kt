package com.zaze.hook.checker

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.TextView
import androidx.annotation.ColorInt
import com.zaze.hook.checker.log.CheckerLogClient
import com.zaze.utils.FileUtil
import com.zaze.utils.date.DateUtil
import java.util.*

/**
 * 自定义 MDM Log Client
 */
class MyLogClient : CheckerLogClient() {
    private val LOG_FILE_PATH =
        App.getInstance().getLogDir() + "/device_checker_log.log"
    private var textView: TextView? = null

    /**
     * 获取本地已记录的mdm error log
     *
     * @return mdm error log
     */
    val mdmErrorLog: String
        get() = FileUtil.readFromFile(LOG_FILE_PATH).toString()

    fun bind(textView: TextView) {
        this.textView = textView
    }

    fun unBind() {
        this.textView = null
    }

    fun clear() {
        textView?.let {
            it.text = ""
            it.scrollTo(0, 0)
        }
        FileUtil.deleteFile(LOG_FILE_PATH)
    }

    private fun log(tag: String, msg: String, @ColorInt colorInt: Int) {
        log(SpannableString("$tag: $msg"), colorInt)
    }

    /**
     * Log results to a textView in application UI
     */
    private fun log(text: SpannableString, @ColorInt colorInt: Int) {
        textView?.post {
            textView?.let {
                text.setSpan(
                    ForegroundColorSpan(colorInt),
                    0,
                    text.length,
                    SpannableString.SPAN_INCLUSIVE_INCLUSIVE
                )
                it.append(text)
                it.append("\n\n")
                val offset = it.lineCount * it.lineHeight
                val height = it.height
                if (offset > height) {
                    // 超出了TextView的范围
                    it.scrollTo(0, offset - height)
                }
                it.invalidate()
            }
        }
    }

    // --------------------------------------------------
    override fun v(tag: String, msg: String) {
        Log.v(tag, msg)
//        log(tag, msg, Color.parseColor("#000000"))
    }

    override fun d(tag: String, msg: String) {
        Log.d(tag, msg)
        log(tag, msg, Color.BLUE)
    }

    override fun i(tag: String, msg: String) {
        Log.i(tag, msg)
        log(tag, msg, Color.parseColor("#008B38"))
    }

    override fun w(tag: String, msg: String) {
        Log.w(tag, msg)
        log(tag, msg, Color.parseColor("#C58F02"))
        dumpMdmError(tag, msg)
    }

    override fun w(tag: String, msg: String, throwable: Throwable) {
        Log.w(tag, msg, throwable)
        log(tag, msg + "\n >>> " + throwable.message, Color.parseColor("#C58F02"))
        dumpMdmError(tag, msg + "\n >>> " + throwable.message)
    }

    override fun e(tag: String, msg: String) {
        Log.e(tag, msg)
        log(tag, msg, Color.RED)
        dumpMdmError(tag, msg)
    }

    override fun e(tag: String, msg: String, throwable: Throwable) {
        Log.e(tag, msg, throwable)
        log(tag, msg + "\n >>> " + throwable.message, Color.RED)
        dumpMdmError(tag, msg + "\n >>> " + throwable.message)
    }

    /**
     * 将错误信息写入sdcard
     *
     * @param tag     tag
     * @param message message
     */
    private fun dumpMdmError(tag: String, message: String) {
        val log = String.format(
            "%s %s %s\n---------\n",
            DateUtil.timeMillisToString(
                System.currentTimeMillis(),
                "yyyy-MM-dd HH:mm:ss",
                TimeZone.getDefault()
            ),
            tag,
            message
        )
//        ThreadPlugins.runInLogThread(Runnable {
//            FileUtil.writeToFile(
//                LOG_FILE_PATH,
//                log,
//                10L shl 20
//            )
//        })
    }
}