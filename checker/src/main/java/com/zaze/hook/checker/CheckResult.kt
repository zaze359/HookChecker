package com.zaze.hook.checker

/**
 * Description :
 * @author : ZAZE
 * @version : 2020-02-19 - 14:33
 */
class CheckResult {

    /**
     * flag > 0 表示存在问题
     */
    private var flags = 0
    /**
     *
     */
    var messageBuilder = StringBuilder()

    fun addError(message: String) {
        flags++
        messageBuilder.append("$message \n")

    }

    fun isError(): Boolean {
        return flags > 0
    }

    fun clear() {
        flags = 0
        messageBuilder.clear()
    }


}