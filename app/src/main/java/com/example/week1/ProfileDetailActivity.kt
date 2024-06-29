package com.example.week1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.week1.databinding.ActivityMainBinding
import com.example.week1.databinding.ProfileDetailBinding
//import kotlinx.android.synthetic.main.activity_profile_detail.*
import com.google.gson.Gson

class ProfileDetailActivity : AppCompatActivity() {
    lateinit var datas : ProfileData
    private lateinit var binding: ProfileDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.profile_detail) as ProfileDetailBinding

        val json = intent.getStringExtra("data")
        val gson = Gson()
        datas = gson.fromJson(json, ProfileData::class.java)

        Glide.with(this).load(datas.img).into(binding.imgProfile)
        binding.tvName.text = datas.name
        binding.tvBirthday.text = datas.bd
    }
}