package com.qytech.systeminforeader

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.File
import java.util.*


@SuppressLint("MissingPermission", "NewApi", "HardwareIds")

@Composable
fun HomeScreen() {
    val macAddress = remember { getMacAddress() }
    val serialNumber = remember { getSerialNumber() }
    val barcodeMacAddress = remember { createBarcode(macAddress) }
    val barcodeSerialNumber = remember { createBarcode(serialNumber) }
    LazyColumn(
        modifier = Modifier.padding(all = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            if (barcodeSerialNumber != null) {
                Image(bitmap = barcodeSerialNumber.asImageBitmap(), contentDescription = "")
                Text(
                    text = "SN: $serialNumber",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(128.dp))
            if (barcodeMacAddress != null) {
                Image(bitmap = barcodeMacAddress.asImageBitmap(), contentDescription = "")
                Text(
                    text = "MAC: $macAddress",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun getMacAddress(): String = runCatching {
    val file = File("/sys/class/net/wlan0/address")
    return file.readText()
}.getOrDefault("")

@SuppressLint("NewApi")
fun getSerialNumber(): String {
    return Build.getSerial()
}

fun createBarcode(content: String, width: Int = 500, height: Int = 200): Bitmap? = runCatching {
    val hints: MutableMap<EncodeHintType, Any> = EnumMap(EncodeHintType::class.java)
    hints[EncodeHintType.CHARACTER_SET] = "utf-8"
    // 容错级别 这里选择最高H级别
    hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
    val bitMatrix = MultiFormatWriter()
        .encode(content, BarcodeFormat.CODE_128, width, height, hints)

    val pixels = IntArray(width * height)
    (0 until height).forEach { y ->
        (0 until width).forEach { x ->
            if (bitMatrix.get(x, y)) {
                pixels[y * width + x] = -0x01000000
            }
        }
    }
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
        setPixels(pixels, 0, width, 0, 0, width, height)
    }
}.getOrNull()