package com.example.week1
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GalleryactivityAdapter(val items: MutableList<GalleryData>, private val context: Context) : RecyclerView.Adapter<GalleryactivityAdapter.ViewHolder>() {

    var datap = mutableListOf<GalleryData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryactivityAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_rv_horizontal,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datap.size

    override fun onBindViewHolder(holder: GalleryactivityAdapter.ViewHolder, position: Int) {
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
//    lateinit var imageList: ArrayList<Uri>
//    lateinit var context: Context
//
//    constructor(imageList: ArrayList<Uri>, context: Context): this() {
//        this.imageList = imageList
//        this.context = context
//
//    }
//
//    //화면 설정 //
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
//
//        val view: View = inflater.inflate(R.layout.item_rv_horizontal, parent, false)
//        return ViewHolder(view)
//
//    }
//
//
//
//    // 데이터 설정 //
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        Glide.with(context)
//            .load(imageList[position]) //이미지 위치
//            .into(holder.TestGalleryAdapter) //보여줄 위치
//    }
//    //아이템 개수//
//    override fun getItemCount(): Int {
//        return imageList.size
//    }
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
//        val TestGalleryAdapter: ImageView= view.findViewById(R.id.galleryView)
//    }
//
//
//}
