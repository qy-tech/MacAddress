package com.qytech.systeminforeader

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.text.TextUtils
import com.qytech.systeminforeader.extensions.starter

class WatchdogService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (TextUtils.equals("mount", intent?.getStringExtra("EXTRA_KEY_TESTFROM"))) {
            starter(MainActivity::class.java)
        }
        return super.onStartCommand(intent, flags, startId)
    }
}

