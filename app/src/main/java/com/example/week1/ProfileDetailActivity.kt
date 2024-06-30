package com.example.week1
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.week1.databinding.ActivityMainBinding
import com.example.week1.databinding.ItemRvSnsBinding
import com.example.week1.databinding.ProfileDetailBinding
//import kotlinx.android.synthetic.main.activity_profile_detail.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson

class ProfileDetailActivity : AppCompatActivity() {
    lateinit var datas : ProfileData
    private lateinit var binding: ProfileDetailBinding
    private lateinit var binding1: ItemRvSnsBinding

    lateinit var snsAdapter: SnsAdapter // 추가함
    val datam = mutableListOf<SnsData>() // 추가함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding1 = DataBindingUtil.setContentView(this, R.layout.item_rv_sns) as ItemRvSnsBinding
        binding = DataBindingUtil.setContentView(this, R.layout.profile_detail) as ProfileDetailBinding

        val json = intent.getStringExtra("data")
        val gson = Gson()
        datas = gson.fromJson(json, ProfileData::class.java)

        Glide.with(this).load(datas.img).into(binding.imgProfile)
        binding.tvName.text = datas.name
        binding.tvBirthday.text = datas.bd

        initRecycler()
    }

    private fun initRecycler() {
        snsAdapter = SnsAdapter(this) // 변경된 이름
        binding.rvSns.adapter = snsAdapter // 변경된 이름

        when (datas.name) { //각 프로필에 따라 다른 데이터 설정
            "Seowon" -> {
                datam.apply {
                    clear()
                    add(SnsData(imgIcon = R.drawable.callicon, num = "010-2923-4581"))
                    add(SnsData(imgIcon = R.drawable.msgicon, num = "010-2923-4581"))
                    add(SnsData(imgIcon = R.drawable.instaicon, num = "shinseon_1030"))
                }
            }
            "Suhwan" -> {
                datam.apply {
                    clear()
                    add(SnsData(imgIcon = R.drawable.callicon, num = "010-6405-9334"))
                    add(SnsData(imgIcon = R.drawable.msgicon, num = "010-6405-9334"))
                    add(SnsData(imgIcon = R.drawable.instaicon, num = "k._swan"))
                }
            }
            // 다른 프로필들에 대해서도 필요에 맞게
            else -> {
                datam.apply {
                    clear()
                    add(SnsData(imgIcon = R.drawable.callicon, num = "010-6468-5953"))
                    add(SnsData(imgIcon = R.drawable.msgicon, num = "010-6468-5953"))
                    add(SnsData(imgIcon = R.drawable.instaicon, num = "abbyycha_"))
                }
            }
        }
        snsAdapter.datas = datam
        snsAdapter.notifyDataSetChanged()
    }
}