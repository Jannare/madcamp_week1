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
import com.google.gson.Gson

class ProfileAdapter(private val context: Context) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {
    var datas = mutableListOf<ProfileData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recycler,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
        //였던 걸 아래처럼 바꿨는데 사실 필요 없는 짓이었던 거 같은 ...

        //holder.txtName.text = datas[position].name
        //holder.txtBd.text = datas[position].bd
        //holder.imgProfile.setImageResource(datas[position].img))
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtName: TextView = itemView.findViewById(R.id.tv_rv_name)
        val txtBd: TextView = itemView.findViewById(R.id.tv_rv_bd)
        val imgProfile: ImageView = itemView.findViewById(R.id.img_rv_photo)
        fun bind(item: ProfileData) {
            txtName.text = item.name
            txtBd.text = item.bd
            Glide.with(itemView).load(item.img).into(imgProfile)

            itemView.setOnClickListener {
                val gson = Gson()
                val profileDetailActivityIntent = Intent(context, ProfileDetailActivity::class.java).apply {
                    putExtra("data", gson.toJson(item))
                }
                context.startActivity(profileDetailActivityIntent)
            }
        }
    }

    fun setFilteredList(datas: MutableList<ProfileData>) {
        this.datas = datas
        notifyDataSetChanged()
    }
}

//
//잘 되었던 옛날 버전 -> RecyclerView.Adapter로 한 번 더 감싸야 하나 싶어서 만들어보는 중.
//var datas = mutableListOf<ProfileData>()
//override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//    val view = LayoutInflater.from(context).inflate(R.layout.item_recycler,parent,false)
//    return ViewHolder(view)
//}
//override fun getItemCount(): Int = datas.size
//override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//    holder.bind(datas[position]) //datas[position]에 있는 구체적인 ProfileData를 가져와서 holder의 bind라는 method로 bind를 해준 것.
//
//}
//inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) { //private으로 둔 거 없앰. 위에서 쓰려고
//    private val txtName: TextView = itemView.findViewById(R.id.tv_rv_name)
//    private val txtBd: TextView = itemView.findViewById(R.id.tv_rv_bd)
//    private val imgProfile: ImageView = itemView.findViewById(R.id.img_rv_photo)
//    fun bind(item: ProfileData) {
//        txtName.text = item.name
//        txtBd.text = item.bd.toString()
//        Glide.with(itemView).load(item.img).into(imgProfile)
//
//        itemView.setOnClickListener {
//            val gson = Gson()
//            val profileDetailActivityIntent = Intent(context, ProfileDetailActivity::class.java).apply {
//                putExtra("data", gson.toJson(item))
//            }
//            context.startActivity(profileDetailActivityIntent)
//        }
//    }
//}