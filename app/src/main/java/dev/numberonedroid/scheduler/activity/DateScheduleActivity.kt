package dev.numberonedroid.scheduler.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.numberonedroid.scheduler.databinding.ActivityDateScheduleBinding

class DateScheduleActivity : AppCompatActivity() {
    lateinit var binding: ActivityDateScheduleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDateScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    fun init(){
        binding.backbtn.setOnClickListener {
            finish()
        }
    }
}