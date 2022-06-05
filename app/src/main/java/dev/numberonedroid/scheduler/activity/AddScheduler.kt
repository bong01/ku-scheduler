package dev.numberonedroid.scheduler.activity

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.numberonedroid.scheduler.R
import dev.numberonedroid.scheduler.db.MyDBHelper
import dev.numberonedroid.scheduler.model.MyData
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
            stime.text = "${shour} : ${smin}"
            etime.text = "${ehour} : ${emin}"
        }
        startbutton.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSet = TimePickerDialog.OnTimeSetListener { timePicker, hour, min ->
                stime.text = "${hour} : ${min}"
                shour = hour
                smin = min
            }
            TimePickerDialog(this, timeSet, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
        endbutton.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSet = TimePickerDialog.OnTimeSetListener { timePicker, hour, min ->
                etime.text = "${hour} : ${min}"
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
                }
                finish()
            }
        }
        addbackbutton.setOnClickListener {
            val intent = Intent(this, SecondMainActivity::class.java)
            startActivity(intent)
        }

    }

}