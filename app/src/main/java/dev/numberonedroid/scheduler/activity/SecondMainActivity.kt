package dev.numberonedroid.scheduler.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.numberonedroid.scheduler.R
import dev.numberonedroid.scheduler.adapter.MyAdapter
import dev.numberonedroid.scheduler.databinding.ActivitySecondMainBinding
import dev.numberonedroid.scheduler.db.MyDBHelper
import dev.numberonedroid.scheduler.model.MyData
import java.io.FileOutputStream

class SecondMainActivity : AppCompatActivity() {
    lateinit var binding: ActivitySecondMainBinding
    lateinit var adapter: MyAdapter
    lateinit var myDBHelper: MyDBHelper
    var YEAR: Int = 0
    var MONTH: Int = 0
    var DAY: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondMainBinding.inflate(layoutInflater)
        //supportActionBar?.setDisplayShowTitleEnabled(false)
        YEAR = intent.getIntExtra("year", 0)
        MONTH = intent.getIntExtra("month", 0)
        DAY = intent.getIntExtra("day", 0)
        supportActionBar?.title = "${MONTH}월 ${DAY}일"
        setContentView(binding.root)
        initData()
        init()
    }

    override fun onResume() {
        super.onResume()
        initData()
        init()
    }

    private fun init() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL)) //줄 긋기
        myDBHelper = MyDBHelper(this)
        val data = myDBHelper.showSchedule(YEAR, MONTH, DAY)
        adapter = MyAdapter(data)
        if(adapter.items.size==0){
            binding.recyclerView.isVisible=false
            binding.emptyView.isVisible=true
        }
        else{
            binding.recyclerView.isVisible=true
            binding.emptyView.isVisible=false
        }
        adapter.itemClickListener = object : MyAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int) {
                val builder = AlertDialog.Builder(this@SecondMainActivity)
                builder.setTitle("일정 수정 및 삭제")
                    .setPositiveButton("수정", DialogInterface.OnClickListener { dlg, _ ->
                        val intent1 = Intent(this@SecondMainActivity, AddScheduler::class.java)
                        intent1.putExtra(
                            "fixsch", MyData(
                                data[position].id,
                                data[position].year,
                                data[position].month,
                                data[position].day,
                                data[position].title,
                                data[position].content,
                                data[position].starthour,
                                data[position].startmin,
                                data[position].endhour,
                                data[position].endmin,
                                data[position].isDone
                            )
                        )
                        startActivity(intent1)
                    })
                    .setNegativeButton("삭제", DialogInterface.OnClickListener { dlg, _ ->
                        myDBHelper.deleteSchedule(data[position].id!!)
                        adapter.removeItem(position)
                        if(adapter.items.size==0)
                            binding.recyclerView.isVisible=false
                            binding.emptyView.isVisible=true
                    })
                val dlg = builder.create()
                dlg.show()
            }
        }
        binding.recyclerView.adapter = adapter
        binding.plusbutton.setOnClickListener {
            val intent = Intent(this, AddScheduler::class.java)
            intent.putExtra("year", getIntent().getIntExtra("year", 0))
            intent.putExtra("month", getIntent().getIntExtra("month", 0))
            intent.putExtra("day", getIntent().getIntExtra("day", 0))
            startActivity(intent)
        }
        binding.backbutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initData() {
        val dbfile = getDatabasePath("mysch.db")
        if (!dbfile.parentFile.exists()) {
            dbfile.parentFile.mkdir()
        }
        if (!dbfile.exists()) {
            val file = resources.openRawResource(R.raw.mysch)
            val fillSize = file.available()
            val buffer = ByteArray(fillSize)
            file.read(buffer)
            file.close()
            dbfile.createNewFile()
            val output = FileOutputStream(dbfile)
            output.write(buffer)
            output.close()
        }
//        dbHelper = DBHelper(this)
//        //날짜 받기
//        data = dbHelper.getAllSchedules()
//        adapter.addItem(data)
//        if(intent.hasExtra("newsch")) {
//            val newdata = intent.getSerializableExtra("newsch") as MyData
//            myDBHelper.insertSchedule(newdata)
//            adapter.addItem(newdata)
//        }
//        data.add(MyData(0,0,0,"dd","ss",1,1,1,1))
//        data.add(MyData(0,0,0,"ss","ss",2,2,2,2))


    }
}