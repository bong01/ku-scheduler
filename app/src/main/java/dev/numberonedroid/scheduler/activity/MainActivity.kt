package dev.numberonedroid.scheduler.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import dev.numberonedroid.scheduler.adapter.CalendarGridAdapter
import dev.numberonedroid.scheduler.databinding.ActivityMainBinding
import dev.numberonedroid.scheduler.databinding.YearMonthPickerBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var adapter: CalendarGridAdapter

    //val monthStr = arrayOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEPT", "OCT", "NOV", "DEC")
    var currentDate = Calendar.getInstance()
    var position = currentDate.get(Calendar.MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        updateCalendar()

        binding.apply {
            thisMonth.setOnClickListener {

                val dlgBinding = YearMonthPickerBinding.inflate(layoutInflater)
                val dlgBuilder = AlertDialog.Builder(this@MainActivity)

                val curyear = currentDate.get(Calendar.YEAR)
                val curmonth = currentDate.get(Calendar.MONTH)

                dlgBinding.monthpicker.maxValue = 12
                dlgBinding.monthpicker.minValue = 1
                dlgBinding.monthpicker.value = curmonth
                dlgBinding.yearpicker.maxValue = Int.MAX_VALUE
                dlgBinding.yearpicker.minValue = 1900
                dlgBinding.yearpicker.value = curyear

                dlgBuilder.setView(dlgBinding.root).setPositiveButton("확인"){
                        _, _ ->
                    val pickyear = dlgBinding.yearpicker.value
                    val pickmonth = dlgBinding.monthpicker.value
                    val pickcalendar = Calendar.getInstance()
                    pickcalendar.set(Calendar.YEAR, pickyear)
                    pickcalendar.set(Calendar.MONTH, pickmonth)

                    val diffyear = pickyear - curyear
                    val diffmonth = diffyear * 12 + (pickmonth - curmonth)
                    position += diffmonth
                    updateCalendar()
                }
                    .setNegativeButton("취소"){
                            _, _ ->
                    }
                    .show()
            }
            prevMonthBtn.setOnClickListener {
                --position
                updateCalendar()
            }
            nextMonthBtn.setOnClickListener {
                ++position
                updateCalendar()
            }
            //TODO 버튼 디자인 수정
            btnToDoList.setOnClickListener {
                startActivity(Intent(this@MainActivity, ToDoListActivity::class.java))
            }
            btnHomepageView.setOnClickListener {
                startActivity(Intent(this@MainActivity, HomepageViewActivity::class.java))
            }
            btnAddSchedule.setOnClickListener {
                startActivity(Intent(this@MainActivity, SecondMainActivity::class.java))
            }
        }

    }

    fun updateCalendar(){
        currentDate.time = Date()
        currentDate.set(Calendar.DAY_OF_MONTH, 1)
        currentDate.set(Calendar.MONTH, position)
        binding.thisMonth.text = "${currentDate.get(Calendar.YEAR)}년 ${currentDate.get(Calendar.MONTH) + 1}월" //${monthStr[currentDate.get(Calendar.MONTH)]}"
        val tempMonth = currentDate.get(Calendar.MONTH)
        val dayList:MutableList<Date> = MutableList(6*7, { Date() })
        for(i in 0..5) {
            for(k in 0..6) {
                currentDate.add(Calendar.DAY_OF_MONTH, (1-currentDate.get(Calendar.DAY_OF_WEEK)) + k)
                dayList[i * 7 + k] = currentDate.time
            }
            currentDate.add(Calendar.WEEK_OF_MONTH, 1)
        }
        binding.calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)
        adapter = CalendarGridAdapter(tempMonth, dayList)
        adapter.onItemClickListener = object: CalendarGridAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                val prevselectedpos = adapter.selectedpos
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