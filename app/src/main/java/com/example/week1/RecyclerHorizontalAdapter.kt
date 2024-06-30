package com.example.week1

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class RecyclerHorizontalAdapter(val items: MutableList<RecyclerModel>, private val context: Context) :
    RecyclerView.Adapter<RecyclerHorizontalAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)

    }

        private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_horizontal, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(items[position])
        holder.itemView.setOnClickListener {
            Log.d("click test", "클릭확인. position = ${holder.itemView.id}")
            itemClickListener.onItemClick(position)

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: RecyclerModel) {
            val imageArea = itemView.findViewById<ImageView>(R.id.galleryView)
//            val titleArea = itemView.findViewById<TextView>(R.id.titleArea)
//            val contentArea = itemView.findViewById<TextView>(R.id.contentArea)
//            val priceArea = itemView.findViewById<TextView>(R.id.priceArea)

            imageArea.setImageResource(item.image)
//            titleArea.text = item.title
//            contentArea.text = item.content
//            priceArea.text = item.price
        }
    }

}
