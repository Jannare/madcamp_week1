package com.example.week1
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GalleryActivityAdapter(private val context: Context) : RecyclerView.Adapter<GalleryActivityAdapter.ViewHolder>() {
    var datap = mutableListOf<GalleryData>()
    var intedatap = mutableListOf<GalleryData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_rv_horizontal,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datap.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datap[position])
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val photoImg: ImageView = itemView.findViewById(R.id.galleryView)
        val photoDate: TextView = itemView.findViewById(R.id.photoDate)

        fun bind(item: GalleryData) {
            photoDate.text = item.date
            Glide.with(itemView).load(item.img).into(photoImg)
        }
    }


}