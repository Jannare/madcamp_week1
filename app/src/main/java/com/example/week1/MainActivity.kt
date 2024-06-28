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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var profileAdapter: ProfileAdapter
    val datas = mutableListOf<ProfileData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        initRecycler1()
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
    private fun initRecycler1() {
        val itemList = mutableListOf<RecyclerModel>()
        itemList.add(RecyclerModel(R.drawable.k1, "대한민국", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.k2, "대한민국", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.k3, "대한민국", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.k4, "대한민국", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.k5, "대한민국", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.k6, "대한민국", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.b1, "대한민국", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.b2, "대한민국", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.b3, "대한민국", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.j1, "일본", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.j2, "일본", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.j3, "일본", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.j4, "일본", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.j5, "일본", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.j6, "일본", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
//        itemList.add(RecyclerModel(R.drawable.J_7, "일본", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.i1, "인도네시아", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.i2, "인도네시아", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
        itemList.add(RecyclerModel(R.drawable.i3, "인도네시아", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))
//        itemList.add(RecyclerModel(R.drawable.item_lv_02, "마리떼 프랑소와 저버", "CLASSIC LOGO WOOL ECO BAG blue", "49,000원"))

        val adapter = RecyclerHorizontalAdapter(itemList)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = GridLayoutManager(this, 2)

        adapter.setItemClickListener(object: RecyclerHorizontalAdapter.onItemClickListener {

            override fun onItemClick(position: Int) {
                Toast.makeText(
                    applicationContext,
                    " ${itemList[position].title}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}