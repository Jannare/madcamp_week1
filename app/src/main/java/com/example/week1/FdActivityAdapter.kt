package com.example.week1
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FdActivityAdapter(private val context: Context) : RecyclerView.Adapter<FdActivityAdapter.ViewHolder>() {
    var dataf = mutableListOf<Fddata>()
    var intedata = mutableListOf<Fddata>()
    var datap = mutableListOf<GalleryData>()
    private var saveddata: MutableList<Fddata.InteGalleryData> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FdActivityAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_rv_fd,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataf.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataf[position])
    }



    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val FolderImg: ImageView = itemView.findViewById(R.id.FolderBtn)
        val Foldername: TextView = itemView.findViewById(R.id.FolderName)
        fun bind(item: Fddata) {
            Foldername.text = item.date
            Glide.with(itemView).load(item.Folderpic).into(FolderImg)
            itemView.setOnClickListener {
                val GalleryActivityIntent = Intent(context, GalleryActivity::class.java)
//                val gson = Gson()
//                if(datap != null){
//                    for (i in datap.indices) {
//                        saveddata.add(Fddata.InteGalleryData(img = datap[i].img.toString(), date = datap[i].date))
//                    }
//                    GalleryActivityIntent.putExtra("gsonData2", gson.toJson(saveddata))
//                }
                context.startActivity(GalleryActivityIntent)
            }
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
