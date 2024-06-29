package com.example.week1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.*
import androidx.recyclerview.widget.GridLayoutManager
//import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week1.databinding.ActivityMainBinding
import com.example.week1.databinding.ActivitySecondBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var profileAdapter: ProfileAdapter
    val datas = mutableListOf<ProfileData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding




        initRecycler()

//        val change22Button: Button = findViewById(R.id.change22Button)
//        val change23Button: Button = findViewById(R.id.change23Button)
        val number = 11.01

        binding.change22Button.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("number",number)
            startActivity(intent)
        }
        binding.change23Button.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra("number",number)
            startActivity(intent)
        }
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
    private fun initRecycler() {
        profileAdapter = ProfileAdapter(this)
        binding.rvProfile.adapter = profileAdapter
        binding.rvProfile.layoutManager = LinearLayoutManager(this)

        datas.apply {
            add(ProfileData(img = R.drawable.profile1, name = "mary", age = 24))
            add(ProfileData(img = R.drawable.profile3, name = "jenny", age = 26))
            add(ProfileData(img = R.drawable.profile2, name = "jhon", age = 27))
            add(ProfileData(img = R.drawable.profile5, name = "ruby", age = 21))
            add(ProfileData(img = R.drawable.profile4, name = "yuna", age = 23))

            profileAdapter.datas = datas
            profileAdapter.notifyDataSetChanged()
        }

    }


}