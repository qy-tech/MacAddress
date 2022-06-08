package com.qytech.systeminforeader.extensions

import android.content.Context
import android.content.Intent

fun Context.starter(cls: Class<*>) {
    val intent = Intent(this, cls)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}
