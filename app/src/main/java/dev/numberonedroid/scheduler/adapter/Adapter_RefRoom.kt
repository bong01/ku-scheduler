package dev.numberonedroid.scheduler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.numberonedroid.scheduler.R
import dev.numberonedroid.scheduler.model.Data_RefRoom

class Adapter_RefRoom(val items:ArrayList<Data_RefRoom>): RecyclerView.Adapter<Adapter_RefRoom.ViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(data: Data_RefRoom, position: Int)
    }
    var itemClickListener:OnItemClickListener? = null
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val homeNameTextView = itemView.findViewById<TextView>(R.id.homenameText)
        val homeUriTextView = itemView.findViewById<TextView>(R.id.homeUriText)
        init {
            homeNameTextView.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition], adapterPosition)
            }
            homeUriTextView.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.homepage_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.homeNameTextView.text = items[position].homeName
        holder.homeUriTextView.text = items[position].homeUri
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun moveItem(oldPos:Int, newPos:Int){
        val item = items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos, item)
        notifyItemMoved(oldPos, newPos)
    }

    fun removeItem(pos:Int) {
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }
}