package com.aireader

import android.app.*
import android.content.*
import android.os.*
import java.io.File

class ReaderService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private val interval: Long = 2000 // 2 秒

    private val task = object : Runnable {
        override fun run() {
            checkFile()
            handler.postDelayed(this, interval)
        }
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
        handler.post(task)
    }

    private fun checkFile() {
        val file = File("/storage/emulated/0/AI/OUT/error.txt")

        if (file.exists()) {
            val content = file.readText()

            // 写入剪贴板
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("AI_ERROR", content)
            clipboard.setPrimaryClip(clip)

            // 删除文件
            file.delete()
        }
    }

    private fun startForegroundService() {
        val channelId = "ai_reader_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "AI Reader",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        val notification = Notification.Builder(this, channelId)
            .setContentTitle("AI Reader")
            .setContentText("Monitoring error.txt")
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .build()

        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?) = null
}
