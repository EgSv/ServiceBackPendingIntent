package ru.startandroid.develop.servicebackpendingintent

import android.app.PendingIntent
import android.app.PendingIntent.CanceledException
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MyService : Service() {
    val LOG_TAG = "myLogs"
    var es: ExecutorService? = null
    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "MyService onCreate")
        es = Executors.newFixedThreadPool(2)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "MyService onDestroy")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, "MyService onStartCommand")
        val time = intent.getIntExtra(MainActivity.PARAM_TIME, 1)
        val pi = intent.getParcelableExtra<PendingIntent>(MainActivity.PARAM_PINTENT)
        val mr: MyRun = MyRun(time, startId, pi!!)
        es!!.execute(mr)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    internal inner class MyRun(var time: Int, var startId: Int, var pi: PendingIntent) : Runnable {
        init {
            Log.d(LOG_TAG, "MyRun#$startId create")
        }

        override fun run() {
            Log.d(LOG_TAG, "MyRun#$startId start, time = $time")
            try {

                pi.send(MainActivity.STATUS_START)

                Thread.sleep(time.toLong())

                val intent = Intent().putExtra(MainActivity.PARAM_RESULT, time * 100)
                pi.send(this@MyService, MainActivity.STATUS_FINISH, intent)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: CanceledException) {
                e.printStackTrace()
            }
            stop()
        }

        fun stop() {
            Log.d(LOG_TAG, "MyRun#" + startId + " end, stopSelfResult("
                    + startId + ") = " + stopSelfResult(startId))
        }
    }
}