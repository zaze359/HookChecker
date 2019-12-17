package com.zaze.hook.xposed.devices

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-12-13 - 16:33
 */
class DeviceInfo {

    var deviceId: String? = null
    var deviceModel: String? = null


    fun copy(deviceInfo: DeviceInfo?) {
        deviceInfo?.let {
            deviceId = it.deviceId
            deviceModel = it.deviceModel
        }

    }

}