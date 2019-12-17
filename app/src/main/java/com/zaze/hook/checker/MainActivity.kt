package com.zaze.hook.checker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zaze.hook.xposed.devices.DeviceHookActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkDeviceBtn.setOnClickListener {
            DeviceChecker.detectSafely(this)
        }

        changedDeviceInfo.setOnClickListener {
            startActivity(Intent(this, DeviceHookActivity::class.java))
        }
    }
}
