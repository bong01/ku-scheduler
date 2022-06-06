package dev.numberonedroid.scheduler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.numberonedroid.scheduler.databinding.RowBinding
import dev.numberonedroid.scheduler.model.MyData
import java.text.DecimalFormat

class MyAdapter(val items:ArrayList<MyData>):RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(position:Int)
    }
    fun removeItem(pos:Int){
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }
    fun addItem(myData: MyData){
        items.add(myData)
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
        val myform = DecimalFormat("00")
        holder.binding.textView.text = items[position].title+"    "+myform.format(items[position].starthour)+":"+
                myform.format(items[position].startmin)+" ~ "+myform.format(items[position].endhour)+":"+
                myform.format(items[position].endmin)
    }


}