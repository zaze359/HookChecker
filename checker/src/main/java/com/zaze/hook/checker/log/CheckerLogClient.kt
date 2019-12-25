package com.zaze.hook.checker.log

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-05-24 - 15:33
 */
open class CheckerLogClient {

    open fun v(tag: String, msg: String) {}
    open fun d(tag: String, msg: String) {}
    open fun i(tag: String, msg: String) {}
    open fun w(tag: String, msg: String) {}
    open fun w(tag: String, msg: String, tr: Throwable) {}
    open fun e(tag: String, msg: String) {}
    open fun e(tag: String, msg: String, tr: Throwable) {}
}
