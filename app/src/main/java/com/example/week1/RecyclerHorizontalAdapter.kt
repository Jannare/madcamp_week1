package com.example.week1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerHorizontalAdapter(val items: MutableList<RecyclerModel>) :
    RecyclerView.Adapter<RecyclerHorizontalAdapter.ViewHolder>() {
    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    private lateinit var itemClickListener: onItemClickListener

    fun setItemClickListener(itemClickListener: onItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerHorizontalAdapter.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_horizontal, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerHorizontalAdapter.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bindItems(items: RecyclerModel) {
            val imageArea = itemView.findViewById<ImageView>(R.id.imageArea)
            val titleArea = itemView.findViewById<TextView>(R.id.titleArea)
            val contentArea = itemView.findViewById<TextView>(R.id.contentArea)
            val priceArea = itemView.findViewById<TextView>(R.id.priceArea)

            imageArea.setImageResource(items.image)
            titleArea.text = items.title
            contentArea.text = items.content
            priceArea.text = items.price
        }
    }
}