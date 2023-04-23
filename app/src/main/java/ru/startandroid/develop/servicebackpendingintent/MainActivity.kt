package ru.startandroid.develop.servicebackpendingintent

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    val LOG_TAG = "myLogs"
    val TASK1_CODE = 1
    val TASK2_CODE = 2
    val TASK3_CODE = 3
    var tvTask1: TextView? = null
    var tvTask2: TextView? = null
    var tvTask3: TextView? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvTask1 = findViewById<View>(R.id.tvTask1) as TextView
        tvTask1!!.text = "Task1"
        tvTask2 = findViewById<View>(R.id.tvTask2) as TextView
        tvTask2!!.text = "Task2"
        tvTask3 = findViewById<View>(R.id.tvTask3) as TextView
        tvTask3!!.text = "Task3"
    }

    fun onClickStart(v: View?) {
        var pi: PendingIntent?
        var intent: Intent

        var intent2 = Intent()

        pi = createPendingResult(TASK1_CODE, intent2, 0)
        intent = Intent(this, MyService::class.java).putExtra(PARAM_TIME, 7000)
            .putExtra(PARAM_PINTENT, pi)
        startService(intent)
        pi = createPendingResult(TASK2_CODE, intent2, 0)
        intent = Intent(this, MyService::class.java).putExtra(PARAM_TIME, 4000)
            .putExtra(PARAM_PINTENT, pi)
        startService(intent)
        pi = createPendingResult(TASK3_CODE,  intent2, 0)
        intent = Intent(this, MyService::class.java).putExtra(PARAM_TIME, 6000)
            .putExtra(PARAM_PINTENT, pi)
        startService(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(LOG_TAG, "requestCode = " + requestCode + ", resultCode = "
                + resultCode)

        if (resultCode == STATUS_START) {
            when (requestCode) {
                TASK1_CODE -> tvTask1!!.text = "Task1 start"
                TASK2_CODE -> tvTask2!!.text = "Task2 start"
                TASK3_CODE -> tvTask3!!.text = "Task3 start"
            }
        }

        if (resultCode == STATUS_FINISH) {
            val result = data!!.getIntExtra(PARAM_RESULT, 0)
            when (requestCode) {
                TASK1_CODE -> tvTask1!!.text = "Task1 finish, result = $result"
                TASK2_CODE -> tvTask2!!.text = "Task2 finish, result = $result"
                TASK3_CODE -> tvTask3!!.text = "Task3 finish, result = $result"
            }
        }
    }

    companion object {
        const val STATUS_START = 100
        const val STATUS_FINISH = 200
        const val PARAM_TIME = "time"
        const val PARAM_PINTENT = "pendingIntent"
        const val PARAM_RESULT = "result"
    }
}