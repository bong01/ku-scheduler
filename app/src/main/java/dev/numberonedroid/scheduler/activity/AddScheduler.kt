package dev.numberonedroid.scheduler.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.numberonedroid.scheduler.R
import dev.numberonedroid.scheduler.db.MyDBHelper
import dev.numberonedroid.scheduler.model.MyData
import dev.numberonedroid.scheduler.util.Constant
import dev.numberonedroid.scheduler.util.ScheduleReceiver
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class AddScheduler : AppCompatActivity() {
    var YEAR: Int = 0
    var MONTH: Int = 0
    var DAY: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        YEAR = intent.getIntExtra("year", 0)
        MONTH = intent.getIntExtra("month", 0)
        DAY = intent.getIntExtra("day", 0)
        init()
    }

    private fun init() {
        val titletext = findViewById<EditText>(R.id.titletext)
        val contenttext = findViewById<EditText>(R.id.contenttext)
        val addbackbutton = findViewById<Button>(R.id.addbackbutton)
        val startbutton = findViewById<Button>(R.id.startbutton)
        val endbutton = findViewById<Button>(R.id.endbutton)
        val savebutton = findViewById<Button>(R.id.savebutton)
        val stime = findViewById<TextView>(R.id.stime)
        val etime = findViewById<TextView>(R.id.etime)
        val myformat = DecimalFormat("00")
        var shour: Int = 0
        var smin: Int = 0
        var ehour: Int = 0
        var emin: Int = 0
        if (intent.hasExtra("fixsch")) {
            val newdata = intent.getSerializableExtra("fixsch") as MyData
            titletext.setText(newdata.title)
            contenttext.setText(newdata.content)
            shour = newdata.starthour
            smin = newdata.startmin
            ehour = newdata.endhour
            emin = newdata.endmin
            stime.text = "${myformat.format(shour)} : ${myformat.format(smin)}"
            etime.text = "${myformat.format(ehour)} : ${myformat.format(emin)}"
        }
        startbutton.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSet = TimePickerDialog.OnTimeSetListener { timePicker, hour, min ->
                stime.text = "${myformat.format(hour)} : ${myformat.format(min)}"
                shour = hour
                smin = min
            }
            TimePickerDialog(this, timeSet, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
        endbutton.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSet = TimePickerDialog.OnTimeSetListener { timePicker, hour, min ->
                etime.text = "${myformat.format(hour)} : ${myformat.format(min)}"
                ehour = hour
                emin = min
            }
            TimePickerDialog(this, timeSet, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        savebutton.setOnClickListener {
            if (titletext.text.toString() == "")
                Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show()
            else if (contenttext.text.toString() == "")
                Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            else {
                Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()
                val myDBHelper = MyDBHelper(this)
                if (intent.hasExtra("fixsch")) {
                    val newdata = intent.getSerializableExtra("fixsch") as MyData
                    myDBHelper.updateSchedule(
                        MyData(
                            id = newdata.id,
                            title = titletext.text.toString(),
                            content = contenttext.text.toString(),
                            year = newdata.year,
                            month = newdata.month,
                            day = newdata.day,
                            starthour = shour,
                            startmin = smin,
                            endhour = ehour,
                            endmin = emin,
                            isDone = newdata.isDone
                        )
                    )
                } else {
                    myDBHelper.insertSchedule(
                        MyData(
                            id = null,
                            title = titletext.text.toString(),
                            content = contenttext.text.toString(),
                            year = YEAR,
                            month = MONTH,
                            day = DAY,
                            starthour = shour,
                            startmin = smin,
                            endhour = ehour,
                            endmin = emin,
                            isDone = false
                        )
                    )

                    val today = Calendar.getInstance()
                    val startDate = "${YEAR}-${MONTH}-${DAY} ${shour}:${smin}:00"
                    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:00")
                    val date = format.parse(startDate) //string to Date
                    val diff = (date.time - today.time.time).toInt()

                    registerNotification(titletext.text.toString(), contenttext.text.toString(), diff.toLong())
                }

                finish()
            }
        }
        addbackbutton.setOnClickListener {
//            val intent = Intent(this, SecondMainActivity::class.java)
//            startActivity(intent)
            // 인텐트로 전환하면 데이터 제대로 출력 안되서 finish로 바꿧습니다!
            finish()
        }

    }

    private fun registerNotification(title:String, content:String, diff:Long) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, ScheduleReceiver::class.java)
        intent.putExtra("title", title)
        intent.putExtra("content", content)
        val pendingIntent = PendingIntent.getBroadcast(
            this, Constant.NOTIFICATION_ID, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = SystemClock.elapsedRealtime() + diff
        Toast.makeText(this, triggerTime.toString(), Toast.LENGTH_SHORT).show()
        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            pendingIntent
        ) // set : 일회성 알림
    }

}