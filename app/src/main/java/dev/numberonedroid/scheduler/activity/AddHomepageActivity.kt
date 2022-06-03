package dev.numberonedroid.scheduler.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.numberonedroid.scheduler.databinding.ActivityAddHomepageBinding
import dev.numberonedroid.scheduler.model.Data_RefRoom
import java.io.PrintStream

class AddHomepageActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddHomepageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.apply {
            addBtn.setOnClickListener {
                val homeName = homeNameEditText.text.toString()
                val homeUri = homeUriEditText.text.toString()
                writeFile(homeName, homeUri)
            }
            clearBtn.setOnClickListener {
                homeNameEditText.text.clear()
                homeUriEditText.text.clear()
                finish()
            }
        }
    }

    private fun writeFile(homeName: String, homeAddress: String) {
        val output = PrintStream(openFileOutput("hompageurls.txt", Context.MODE_APPEND))
        output.println(homeName)
        output.println(homeAddress)
        output.close()
        val intent = Intent()
        intent.putExtra("homepage", Data_RefRoom(homeName, homeAddress))
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}