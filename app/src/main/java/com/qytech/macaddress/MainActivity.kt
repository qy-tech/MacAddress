package com.qytech.macaddress

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.Exception


class MainActivity : Activity() {

    private var mMacAddress: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMacAddress = getMacAddress()
        tv_mac_address.text = mMacAddress
//        val test: TextView? = null
//        test!!.text = "hhhh"
//        while (true){
//            createImage()
//        }
        createImage()
//        rebootCount()
        hideBottomUIMenu()
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    @SuppressLint("ObsoleteSdkInt")
    private fun hideBottomUIMenu() {
        if (Build.VERSION.SDK_INT in 12..18) {
            val view = this.window.decorView
            view.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            val decorView = window.decorView
            val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
            decorView.systemUiVisibility = uiOptions
        }
    }

    companion object {
        private val TAG: String = this::class.java.simpleName

        fun starter(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    private fun createImage() {
        val bitmap: Bitmap?
        try {
            bitmap = createOneDCode(mMacAddress ?: "")
            iv_qr_code.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 用于将给定的内容生成成一维码 注：目前生成内容为中文的话将直接报错，要修改底层jar包的内容
     *
     * @param content 将要生成一维码的内容
     * @return 返回生成好的一维码bitmap
     * @throws WriterException WriterException异常
     */
    @Throws(WriterException::class)
    private fun createOneDCode(content: String): Bitmap {
        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        val matrix = MultiFormatWriter().encode(content,
                BarcodeFormat.CODE_128, 500, 200)
        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = -0x1000000
                }
            }
        }

        val bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888)
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }


    private fun getMacAddress(): String? {
        Log.d(TAG, "get mac address")
        val process: Process = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ")
        val input = LineNumberReader(InputStreamReader(process.inputStream))
        var line: String?
        val result: String?
        while (true) {
            line = input.readLine()
            Log.d(TAG, line)
            if (line != null) {
                result = line
                break
            }
        }
        return result ?: "test"
    }

    private fun rebootCount() {
        val file = File(Environment.getExternalStorageDirectory().path + "/rebootcount.txt")
        if (!file.exists()) {
            file.createNewFile()
        }
        val result = readFile(file)
        var count = Integer.valueOf(result)!!
        writeFile(file, (++count).toString())
    }

    private fun writeFile(file: File, content: String) {
        val outputStream: FileOutputStream
        val buffer: BufferedOutputStream
        try {
            outputStream = FileOutputStream(file)
            buffer = BufferedOutputStream(outputStream)
            buffer.write(content.toByteArray())
            buffer.flush()
            buffer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun readFile(file: File): String {
        var `is`: FileInputStream? = null

        var reader: BufferedReader? = null
        var result = "0"
        try {
            `is` = FileInputStream(file)
            reader = BufferedReader(InputStreamReader(`is`))
            var line: String
            line = reader.readLine()
            while (!TextUtils.isEmpty(line)) {
                result += line
                line = reader.readLine()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                reader!!.close()
                `is`!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return result
    }



}
