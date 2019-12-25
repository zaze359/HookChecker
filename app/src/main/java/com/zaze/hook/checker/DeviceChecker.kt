package com.zaze.hook.checker

import android.content.Context
import com.zaze.common.thread.ThreadPlugins
import com.zaze.hook.checker.log.CheckerLog

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-11-27 - 13:54
 */
object DeviceChecker {

    private const val TAG = "DeviceChecker"

    /**
     * 检测安全
     */
    fun detectSafely(context: Context) {
        var hasXposed = XposedChecker.detectByStackTrace()
        ThreadPlugins.runInIoThread(Runnable {
            val isEmulator = QemuChecker.detectEmulatorByProp(context)
            if (!hasXposed) {
                hasXposed = XposedChecker.detectByClassLoader() or XposedChecker.detectByMaps()
            }
            val isRoot = RootChecker.detectRoot()
//            if (isEmulator) {
//                CheckerLog.e(TAG, "程序运行在模拟器中!")
//            } else {
//                CheckerLog.i(TAG, "程序运行在真实设备中!")
//            }
            CheckerLog.log(TAG, "isEmulator : $isEmulator", isEmulator)
            CheckerLog.log(TAG, "isRoot : $isRoot", isRoot)
            CheckerLog.log(TAG, "hasXposed : $hasXposed", hasXposed)
        })
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