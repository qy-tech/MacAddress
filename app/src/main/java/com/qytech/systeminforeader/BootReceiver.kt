package com.qytech.systeminforeader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import java.io.File

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (TextUtils.equals(Intent.ACTION_MEDIA_MOUNTED, intent.action)) {
            val usbPath = intent.data?.path
            val storage = usbPath?.let { File(it) }
            storage?.listFiles()?.forEachIndexed { _, file ->
                if (TextUtils.equals(file.name, "qrcode.bin")) {
                    val newIntent = Intent(context, WatchdogService::class.java)
                    newIntent.putExtra("EXTRA_KEY_TESTFROM", "mount")
                    context.startService(newIntent)
                    return@forEachIndexed
                }
            }
        }
    }
}