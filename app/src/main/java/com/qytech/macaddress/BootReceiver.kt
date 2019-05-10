package com.qytech.macaddress

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.qytech.macaddress.service.WatchdogService
import java.io.File


/**
 * Created by Administrator on 2018/3/5.
 */
class BootReceiver : BroadcastReceiver() {
    private var mContext: Context? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        mContext = context
        if (TextUtils.equals(Intent.ACTION_MEDIA_MOUNTED, intent?.action)) {
            val usbPath = intent?.data?.path
            val storage = File(usbPath)
            storage.listFiles()?.forEachIndexed { _, file ->
                if (TextUtils.equals(file.name, "qrcode.bin")) {
                    val newIntent = Intent(context, WatchdogService::class.java)
                    newIntent.putExtra("EXTRA_KEY_TESTFROM", "mount")
                    context?.startService(newIntent)
                    return@forEachIndexed
                }
            }
        }
    }
}