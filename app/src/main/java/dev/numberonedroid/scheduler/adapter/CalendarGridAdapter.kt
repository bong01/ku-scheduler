package dev.numberonedroid.scheduler.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.recyclerview.widget.RecyclerView
import dev.numberonedroid.scheduler.databinding.CalendarGridItemBinding
import java.util.*

class CalendarGridAdapter(val tempMonth:Int, val items:MutableList<Date>) : RecyclerView.Adapter<CalendarGridAdapter.ViewHolder>() {

    val rows = 6
    var selectedpos : Int? = null

    interface OnItemClickListener{
        fun OnItemClick(position: Int)
    }

    var onItemClickListener : OnItemClickListener? = null

    inner class ViewHolder(val binding: CalendarGridItemBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.date.setOnClickListener {
                onItemClickListener?.OnItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CalendarGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var tempCal = Calendar.getInstance()
        tempCal.time = items[position]
        holder.binding.date.text = tempCal.get(Calendar.DAY_OF_MONTH).toString()
        val textColor = when(position%7){
            0-> Color.RED
            6-> Color.BLUE
            else-> Color.BLACK
        }
        holder.binding.date.setTextColor(textColor)
        if(tempMonth != tempCal.get(Calendar.MONTH)){
            holder.binding.date.alpha = 0.4f
        }

        if(position == selectedpos){
            holder.binding.date.setBackgroundColor(Color.LTGRAY)
        } else {
            holder.binding.date.setBackgroundColor(Color.TRANSPARENT)
        }

        if(isToday(tempCal)){
            if(position==selectedpos) {
                val r1 = Color.YELLOW.red
                val b1 = Color.YELLOW.blue
                val g1 = Color.YELLOW.green
                val r2 = Color.LTGRAY.red
                val b2 = Color.LTGRAY.blue
                val g2 = Color.LTGRAY.green
                val r3 = (r1 + r2) / 2
                val b3 = (b1 + b2) / 2
                val g3 = (g1 + g2) / 2
                val avgColor = Color.rgb(r3, g3, b3)
                holder.binding.date.setBackgroundColor(avgColor)
            } else
                holder.binding.date.setBackgroundColor(Color.YELLOW)
        }
    }

    override fun getItemCount(): Int {
        //return items.size
        return rows*7
    }

    fun isToday(tempCal:Calendar): Boolean {
        var curCal = Calendar.getInstance()
        curCal.time = Date()
        return tempCal.get(Calendar.DAY_OF_YEAR) == curCal.get(Calendar.DAY_OF_YEAR)
                && tempCal.get(Calendar.YEAR) == curCal.get(Calendar.YEAR)
    }
}