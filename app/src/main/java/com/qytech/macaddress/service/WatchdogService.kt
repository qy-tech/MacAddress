package com.qytech.macaddress.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import com.qytech.macaddress.MainActivity
import com.qytech.macaddress.starter

/**
 * Created by Administrator on 2018/3/5.
 */
class WatchdogService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("WatchdogService","onStartCommand")
        if (TextUtils.equals("mount",intent?.getStringExtra("EXTRA_KEY_TESTFROM"))){
            startMain()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startMain() {
//        MainActivity.starter(this)
        starter(MainActivity::class.java)
    }
}