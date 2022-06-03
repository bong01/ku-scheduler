package dev.numberonedroid.scheduler.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import dev.numberonedroid.scheduler.adapter.CalendarGridAdapter
import dev.numberonedroid.scheduler.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var adapter: CalendarGridAdapter

    var monthStr = arrayOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEPT", "OCT", "NOV", "DEC")
    val currentDate = Calendar.getInstance()
    var position = currentDate.get(Calendar.MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        updateCalendar()
        binding.prevMonthBtn.setOnClickListener {
            --position
            updateCalendar()
        }
        binding.nextMonthBtn.setOnClickListener {
            ++position
            updateCalendar()
        }
    }

    fun updateCalendar(){
        var calendar = currentDate.clone() as Calendar
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.MONTH, position)
        binding.thisMonth.text = "${calendar.get(Calendar.YEAR)} ${monthStr[calendar.get(Calendar.MONTH)]}"
        var tempMonth = calendar.get(Calendar.MONTH)
        var dayList:MutableList<Date> = MutableList(6*7, { Date() })
        for(i in 0..5) {
            for(k in 0..6) {
                calendar.add(Calendar.DAY_OF_MONTH, (1-calendar.get(Calendar.DAY_OF_WEEK)) + k)
                dayList[i * 7 + k] = calendar.time
            }
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
        }
        binding.calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)
        adapter = CalendarGridAdapter(tempMonth, dayList)
        adapter.onItemClickListener = object: CalendarGridAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                var prevselectedpos = adapter.selectedpos
                adapter.selectedpos = position
                adapter.notifyItemChanged(position)
                if(prevselectedpos != null) {
                    adapter.notifyItemChanged(prevselectedpos)
                }
                val intent = Intent(this@MainActivity, DateScheduleActivity::class.java)
                startActivity(intent)
            }
        }
        binding.calendarRecyclerView.adapter = adapter
    }
}