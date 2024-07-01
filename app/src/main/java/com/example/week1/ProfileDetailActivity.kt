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
    val datam = mutableListOf<ProfileData.SnsData>() // 추가함

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

        initRecycler(datas)
    }

    private fun initRecycler(i: ProfileData) {
        snsAdapter = SnsAdapter(this)
        binding.rvSns.adapter = snsAdapter

        datam.apply {  //각 프로필에 따라 다른 데이터 설정
            clear()
            add(ProfileData.SnsData(imgIcon = R.drawable.callicon, number = i.snsData.number, insta = i.snsData.insta))
            add(ProfileData.SnsData(imgIcon = R.drawable.msgicon, number = i.snsData.number, insta = i.snsData.insta))
            add(ProfileData.SnsData(imgIcon = R.drawable.instaicon, number = i.snsData.insta, insta = i.snsData.insta))
        }
        snsAdapter.datas = datam
        snsAdapter.notifyDataSetChanged()
    }
}