package dev.numberonedroid.scheduler.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.numberonedroid.scheduler.adapter.ToDoListAdapter
import dev.numberonedroid.scheduler.databinding.ActivityToDoListBinding
import dev.numberonedroid.scheduler.db.MyDBHelper
import dev.numberonedroid.scheduler.model.MyData

class ToDoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityToDoListBinding
    private lateinit var recyclerView: RecyclerView
    private var schedules: ArrayList<MyData> = ArrayList()
    lateinit var myDBHelper: MyDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityToDoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()
        initScheduleList()
        initRecyclerView()

    }

    private fun initLayout() {
        binding.apply {
            tvMonth.text = intent.getIntExtra("month", 0).toString()
        }
    }

    private fun initScheduleList() {
        //TODO DB에 있는 데이터로 교체하기
        val year = intent.getIntExtra("year", 0)
        val month = intent.getIntExtra("month", 0)
        myDBHelper = MyDBHelper(this)
        schedules = myDBHelper.showSchedule2(year, month) // todo
    }

    private fun initRecyclerView() {
        recyclerView = binding.rvToDoList
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = ToDoListAdapter(schedules)
    }

}