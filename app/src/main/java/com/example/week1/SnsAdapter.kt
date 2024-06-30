package com.example.week1

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson

class SnsAdapter(private val context: Context) : RecyclerView.Adapter<SnsAdapter.ViewHolder>() {

    var datas = mutableListOf<SnsData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_rv_sns,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtNum: TextView = itemView.findViewById(R.id.tv_rv_num)
        private val imgSNS: ImageView = itemView.findViewById(R.id.img_rv_sns)

        fun bind(item: SnsData) {
            txtNum.text = item.num
            Glide.with(itemView).load(item.imgIcon).into(imgSNS)

            itemView.setOnClickListener {
                if (txtNum.text.startsWith("0") && item.imgIcon==R.drawable.callicon) {
                    val callIntent = Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+txtNum.text))
                    context.startActivity(callIntent)
                }
                else if (txtNum.text.startsWith("0") && item.imgIcon==R.drawable.msgicon){
                    val msgIntent = Intent(Intent.ACTION_SENDTO,Uri.parse("sms:"+txtNum.text))
                    context.startActivity(msgIntent)
                }
                else {
                    val instaIntent = Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/"+txtNum.text))
                    context.startActivity(instaIntent)
                }
            }
        }
    }
}
//        itemView.setOnClickListener {
//            val gson = Gson()
//            val profileDetailActivityIntent = Intent(context, ProfileDetailActivity::class.java).apply {
//                putExtra("data", gson.toJson(item))
//            }
//            context.startActivity(profileDetailActivityIntent)
//        }