package dev.numberonedroid.scheduler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.numberonedroid.scheduler.databinding.RowBinding
import dev.numberonedroid.scheduler.model.MyData

class MyAdapter(val items:ArrayList<MyData>):RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(position:Int)
    }
    fun removeItem(pos:Int){
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }
    fun addItem(mydata: MyData){
        items.add(mydata)
        notifyDataSetChanged()
    }

    var itemClickListener: OnItemClickListener?=null

    inner class MyViewHolder(val binding: RowBinding):RecyclerView.ViewHolder(binding.root){
        init{
            binding.textView.setOnClickListener{
                itemClickListener?.OnItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(view)
    }
    override fun getItemCount(): Int {
        return items.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.textView.text = items[position].title+"    "+items[position].starthour+":"+items[position].startmin+" ~ "+items[position].endhour+":"+items[position].endmin
    }


}