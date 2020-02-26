package com.zaze.hook.xposed.view

import com.zaze.hook.xposed.MyXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2020-02-09 - 21:09
 */
class ViewHooker: MyXposedHookLoadPackage() {


    override fun actualHandleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
    }


    private fun hookTextView() {

    }
}