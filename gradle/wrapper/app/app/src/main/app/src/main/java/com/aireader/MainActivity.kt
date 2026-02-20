package com.aireader

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启动后台服务
        startService(Intent(this, ReaderService::class.java))
        // 关闭界面
        finish()
    }
}
