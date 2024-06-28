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
//import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    lateinit var profileAdapter: ProfileAdapter
    val datas = mutableListOf<ProfileData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initRecycler()

        val change22Button: Button = findViewById(R.id.change22Button)
        val change23Button: Button = findViewById(R.id.change23Button)
        val number = 11.01

        change22Button.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("number",number)
            startActivity(intent)
        }
        change23Button.setOnClickListener {
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
        val recyclerView: RecyclerView = findViewById(R.id.rv_profile)
        recyclerView.adapter = profileAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

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