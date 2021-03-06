package com.zaze.hook.xposed.devices

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zaze.hook.xposed.R
import com.zaze.hook.xposed.data.XposedSharedPreferencesHelper
import kotlinx.android.synthetic.main.device_hook_act.*

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-13 - 16:48
 */
class DeviceHookActivity : Activity() {
    private var deviceInfo: DeviceInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.device_hook_act)
        if (checkAndReqPermission()) {
            init()
        }
    }

    fun init() {
        deviceInfo = XposedSharedPreferencesHelper.getCustomDeviceInfo(this)
            ?: DeviceInfo(
                DeviceUtil.getUUID(this), Build.MODEL
            )
        hookDeviceIdEt.setText(deviceInfo?.deviceId)
        hookDeviceNameEt.setText(deviceInfo?.deviceModel)
        hookDeviceInfoSaveBtn.setOnClickListener {
            deviceInfo?.copy(
                deviceId = hookDeviceIdEt.text.toString(),
                deviceModel = hookDeviceNameEt.text.toString()
            )
            XposedSharedPreferencesHelper.saveCustomDeviceInfo(this, deviceInfo)
        }
    }

    fun checkAndReqPermission(): Boolean {
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!grantResults.contains(PackageManager.PERMISSION_DENIED)) {
            init()
        }
    }
}