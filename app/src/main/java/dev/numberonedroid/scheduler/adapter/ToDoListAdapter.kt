package dev.numberonedroid.scheduler.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.numberonedroid.scheduler.databinding.ToDoListItemBinding
import dev.numberonedroid.scheduler.model.Schedule

class ToDoListAdapter (val scheduleList: List<Schedule>) : RecyclerView.Adapter<ToDoListAdapter.ViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(schedule: Schedule)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: ToDoListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener{
                itemClickListener?.onItemClick(scheduleList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ToDoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            cbIsDone.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    tvTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    tvTitle.paintFlags = 0
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

}