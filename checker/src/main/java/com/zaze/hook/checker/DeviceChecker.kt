package com.zaze.hook.checker

import android.content.Context

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-11-27 - 13:54
 */
object DeviceChecker {

    /**
     * 检测安全
     */
    fun detectSafely(context: Context) {
        QemuChecker.detectEmulatorByProp(context)
        DebuggerChecker.detectDebugger(context)
    }

//    public static boolean CheckRootPathSU()
//    {
//        File f=null;
//        final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/","/vendor/bin/"};
//        try{
//            for(int i=0;i<kSuSearchPaths.length;i++)
//            {
//                f=new File(kSuSearchPaths[i]+"su");
//                if(f!=null&&f.exists())
//                {
//                    return true;
//
//                }
//            }catch(Exception e)
//            {
//                e.printStackTrace();
//            }
//            return false;
//        }

//    fun checkRootWhichSU(): Boolean {
//        val strCmd = arrayOf("/system/xbin/which", "su")
//        val execResult = executeCommand(strCmd)
//        return if (execResult != null) {
//            true
//        } else {
//            false
//        }
//    }

}