package dev.numberonedroid.scheduler.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.recyclerview.widget.RecyclerView
import dev.numberonedroid.scheduler.R
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
            holder.binding.date.setBackgroundColor(ContextCompat.getColor(holder.binding.date.context,R.color.selectday))
        } else {
            holder.binding.date.setBackgroundColor(ContextCompat.getColor(holder.binding.date.context,R.color.calendargrid))
        }

        if(isToday(tempCal)){
            if(position==selectedpos) {
                val c1 = ContextCompat.getColor(holder.binding.date.context,R.color.today)
                val c2 = ContextCompat.getColor(holder.binding.date.context,R.color.selectday)
                val a3 = c1.alpha //(c1.alpha + c2.alpha) / 2
                val r3 = (c1.red + c2.red) / 2
                val g3 = (c1.green + c2.green) / 2
                val b3 = (c1.blue + c2.blue) / 2
                val avgColor = Color.argb(a3, r3, g3, b3)
                holder.binding.date.setBackgroundColor(avgColor)
            } else
                holder.binding.date.setBackgroundColor(ContextCompat.getColor(holder.binding.date.context,R.color.today))
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