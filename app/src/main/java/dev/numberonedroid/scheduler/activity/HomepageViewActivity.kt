package dev.numberonedroid.scheduler.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.numberonedroid.scheduler.R
import dev.numberonedroid.scheduler.adapter.Adapter_RefRoom
import dev.numberonedroid.scheduler.databinding.ActivityHomepageViewBinding
import dev.numberonedroid.scheduler.model.Data_RefRoom
import java.io.File
import java.io.PrintStream
import java.lang.Exception
import java.util.*

class HomepageViewActivity : AppCompatActivity() {
    lateinit var binding:ActivityHomepageViewBinding
    var data:ArrayList<Data_RefRoom> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter:Adapter_RefRoom

    val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK) {
            val homepage = it.data?.getSerializableExtra("homepage") as Data_RefRoom
            Toast.makeText(this, homepage.homeName + " 홈페이지 추가됨!", Toast.LENGTH_SHORT).show()
            data.add(Data_RefRoom(homepage.homeName, homepage.homeUri))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initRecyclerView()
        initLayout()

    }

    private fun initLayout() {
        binding.apply {
            openFileBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/pdf"

                    putExtra(DocumentsContract.EXTRA_INITIAL_URI, 2)
                }

                startActivity(intent)
            }
            addHomeBtn.setOnClickListener {
                val intent2 = Intent(this@HomepageViewActivity, AddHomepageActivity::class.java)
                activityResultLauncher.launch(intent2)
            }
        }
    }

    private fun initData() {
        try {
            val outputScan = Scanner(openFileInput("out.txt"))
            readFileScan(outputScan)
        } catch (e: Exception){
            Toast.makeText(this, "아직 추가된 홈페이지가 없습니다.",Toast.LENGTH_SHORT).show()
        }
    }

    private fun readFileScan(scan: Scanner){
        while(scan.hasNext()) {
            val homeName = scan.nextLine()
            val homeAddress = scan.nextLine()
            data.add(Data_RefRoom(homeName, homeAddress))
        }
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = Adapter_RefRoom(data)
        adapter.itemClickListener = object :Adapter_RefRoom.OnItemClickListener{
            override fun OnItemClick(data: Data_RefRoom, position: Int) {
                //홈페이지로 이동!
                val weppage = Uri.parse(data.homeUri)
                val intent3 = Intent(Intent.ACTION_VIEW, weppage)
                startActivity(intent3)
            }
        }
        recyclerView.adapter = adapter

        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT) {
            override fun onMove(
                p0: RecyclerView,
                p1: RecyclerView.ViewHolder,
                p2: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(p1.adapterPosition, p2.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var pos = viewHolder.adapterPosition
                showDataRemoveAlert(pos)
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun showDataRemoveAlert(pos: Int) {
        AlertDialog.Builder(this)
            .setTitle("해당 홈페이지 링크 정보를 정말 삭제하시겠습니까?")
            .setPositiveButton("삭제") { dialogInterface: DialogInterface, i: Int ->
                adapter.removeItem(pos)
                dataRemove()
                adapter.notifyDataSetChanged()
            }
            .setNegativeButton("취소") { dialogInterface: DialogInterface, i: Int ->
                adapter.notifyDataSetChanged()
            }
            .show()
    }

    private fun dataRemove(){
        val textFile = File("/data/data/com.example.myschedulerksm/files/out.txt")
        textFile.delete()

        for(i in adapter.items.indices) {
            val output = PrintStream(openFileOutput("out.txt", MODE_APPEND))
            output.println(adapter.items[i].homeName)
            output.println(adapter.items[i].homeUri)
            output.close()
        }
    }
}