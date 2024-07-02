package com.example.week1

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
        //datas[position]에 있는 구체적인 ProfileData를 가져와서 holder의 bind라는 method로 bind를 해준 것.
        //아래처럼도 썼다가 .. 위로 합침
        //holder.txtName.text = datas[position].name
        //holder.txtBd.text = datas[position].bd
        //holder.imgProfile.setImageResource(datas[position].img)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) { //위에서 쓰려고 private 없앰.
        val txtName: TextView = itemView.findViewById(R.id.tv_rv_name)
        val txtBd: TextView = itemView.findViewById(R.id.tv_rv_bd)
        val imgProfile: ImageView = itemView.findViewById(R.id.img_rv_photo)
        fun bind(item: ProfileData) {
            txtName.text = item.name

            txtBd.text = item.bd
            Glide.with(itemView).load(Uri.parse(item.img)).into(imgProfile)

            itemView.setOnClickListener {
                val gson = Gson()
                val profileDetailActivityIntent = Intent(context, ProfileDetailActivity::class.java).apply {
                    putExtra("data", gson.toJson(item))
                }
                context.startActivity(profileDetailActivityIntent)
            }

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    AlertDialog.Builder(itemView.context).apply {
                        setTitle("연락처 삭제하기")
                        setMessage("${datas[position].name}을(를) 삭제하시겠습니까?")
                        setPositiveButton("삭제하기") { _, _ ->
                            datas.removeAt(position)
                            notifyDataSetChanged()
                        }
                        setNeutralButton("취소", null)
                        show()
                    }
                }
                false
            }
        }
    }

    fun setFilteredList(datam: MutableList<ProfileData>) {
        this.datas = datam
        notifyDataSetChanged()
    }
}