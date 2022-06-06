package dev.numberonedroid.scheduler.adapter

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.numberonedroid.scheduler.databinding.ToDoListItemBinding
import dev.numberonedroid.scheduler.db.MyDBHelper
import dev.numberonedroid.scheduler.model.MyData
import java.text.SimpleDateFormat
import java.util.*


class ToDoListAdapter(val schedules: List<MyData>) : RecyclerView.Adapter<ToDoListAdapter.ViewHolder>() {

    lateinit var myDBHelper: MyDBHelper

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: ToDoListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        myDBHelper = MyDBHelper(parent.context)
        val view = ToDoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val schedule = schedules[position]

            if (schedule.isDone) {
                tvTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                cbIsDone.isChecked = schedule.isDone
            }

            //체크할 경우 isDone true로 변경
            cbIsDone.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    tvTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    schedule.isDone = true
                    myDBHelper.updateSchedule(schedule)
                } else {
                    tvTitle.paintFlags = 0
                    schedule.isDone = false
                    myDBHelper.updateSchedule(schedule)
                }
            }

            //D-Day 계산
            val today = Calendar.getInstance()
            val startDate = "${schedule.year}-${schedule.month}-${schedule.day - 1} 00:00:00"
            val format = SimpleDateFormat("yyyy-MM-dd 00:00:00")
            val date = format.parse(startDate) //string to Date
            val diff = ((today.time.time - date.time) / (60 * 60 * 24 * 1000)).toInt()

            //이미 지난 일정일 경우 회색
            //D-DAY: 빨간색
            //D-1: 파란색
            tvDDay.text = "D${diff - 1}"
            if ((diff - 1) > 0) {
                tvDDay.setTextColor(Color.GRAY)
                tvTitle.setTextColor(Color.GRAY)
                tvDDay.text = "D+${diff - 1}"
            }
            if ((diff - 1) == 0) {
                tvDDay.text = "D-DAY"
                tvDDay.setTextColor(Color.RED)
                tvTitle.setTextColor(Color.RED)
            } else if ((diff - 1) == -1) {
                tvDDay.setTextColor(Color.BLUE)
                tvTitle.setTextColor(Color.BLUE)
            }


            tvTitle.text = schedule.title
            tvStartAt.text = "${schedule.month}월 ${schedule.day}일 ${schedule.starthour}:${schedule.startmin}"
            tvEndAt.text = "${schedule.month}월 ${schedule.day}일 ${schedule.endhour}:${schedule.endmin}"
        }
    }

    override fun getItemCount(): Int {
        return schedules.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}