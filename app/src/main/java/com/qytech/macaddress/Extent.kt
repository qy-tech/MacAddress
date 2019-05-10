package com.qytech.macaddress

import android.content.Context
import android.content.Intent

/**
 * Created by Administrator on 2018/3/16.
 */
fun Context.starter(cls: Class<*>) {
    val intent = Intent(this, cls)
    startActivity(intent)
}
