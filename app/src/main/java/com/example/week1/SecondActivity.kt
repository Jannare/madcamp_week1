package com.example.week1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.week1.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var binding1: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        binding1 =
            DataBindingUtil.setContentView(this, R.layout.activity_second) as ActivitySecondBinding

        initRecycler1()

        val change21Button: Button = findViewById(R.id.change21Button)
        val change23Button: Button = findViewById(R.id.change23Button)
        var number = 22.02

        change21Button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("number", number)
            startActivity(intent)
        }
        change23Button.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra("number", number)
            startActivity(intent)
        }

        number = intent.getDoubleExtra("number", 0.0)
        Toast.makeText(this, number.toString(), Toast.LENGTH_SHORT).show()
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
        binding1.rvPics.adapter = adapter
        binding1.rvPics.layoutManager = GridLayoutManager(this, 2)

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