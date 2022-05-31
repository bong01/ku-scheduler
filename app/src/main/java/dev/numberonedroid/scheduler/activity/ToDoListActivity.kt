package dev.numberonedroid.scheduler.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.numberonedroid.scheduler.adapter.ToDoListAdapter
import dev.numberonedroid.scheduler.databinding.ActivityToDoListBinding
import dev.numberonedroid.scheduler.model.Schedule
import dev.numberonedroid.scheduler.util.DateUtil
import java.time.LocalDateTime

class ToDoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityToDoListBinding
    private lateinit var recyclerView: RecyclerView
    private var scheduleList: ArrayList<Schedule> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityToDoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initScheduleList()
        initRecyclerView()

    }

    private fun initScheduleList() {
        //TODO DB에 있는 데이터로 교체하기
        scheduleList.add(
            Schedule(
                "모바일 프로그래밍 수업",
                "Firebase 실습",
                DateUtil.localDateTimeToString(LocalDateTime.now()),
                DateUtil.localDateTimeToString(LocalDateTime.now())
            )
        )

        scheduleList.add(
            Schedule(
                "모바일 프로그래밍 수업",
                "Firebase 실습",
                DateUtil.localDateTimeToString(LocalDateTime.now()),
                DateUtil.localDateTimeToString(LocalDateTime.now())
            )
        )

        scheduleList.add(
            Schedule(
                "모바일 프로그래밍 수업",
                "Firebase 실습",
                DateUtil.localDateTimeToString(LocalDateTime.now()),
                DateUtil.localDateTimeToString(LocalDateTime.now())
            )
        )

        scheduleList.add(
            Schedule(
                "모바일 프로그래밍 수업",
                "Firebase 실습",
                DateUtil.localDateTimeToString(LocalDateTime.now()),
                DateUtil.localDateTimeToString(LocalDateTime.now())
            )
        )
    }

    private fun initRecyclerView() {
        recyclerView = binding.rvToDoList
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = ToDoListAdapter(scheduleList)
    }

}