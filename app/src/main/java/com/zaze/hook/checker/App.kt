package com.zaze.hook.checker

import android.app.Application
import com.zaze.hook.checker.log.CheckerLog

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-18 - 13:52
 */
class App : Application() {

    private lateinit var mdmLogClient: MyLogClient

    fun getMdmLogClient(): MyLogClient {
        return mdmLogClient
    }

    companion object {
        private lateinit var INSTANCE: Application

        fun getInstance(): App {
            return INSTANCE as App
        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        mdmLogClient = MyLogClient()
        CheckerLog.setLogClient(mdmLogClient)
    }


    fun getLogDir(): String {
        return this.externalCacheDir!!.absolutePath
    }
}