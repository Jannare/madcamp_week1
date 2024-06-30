package com.example.week1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.week1.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {



    private lateinit var binding1: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding1 = DataBindingUtil.setContentView(this, R.layout.activity_second) as ActivitySecondBinding

        initRecycler1()

        val number = intent.getDoubleExtra("number", 0.1)
        Toast.makeText(this, number.toString(), Toast.LENGTH_SHORT).show()

        binding1.change21Button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("number", number)
            startActivity(intent)
        }

        binding1.change23Button.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra("number", number)
            startActivity(intent)
        }
    }

    private fun initRecycler1() {
        val itemList = mutableListOf<RecyclerModel>()
        itemList.add(RecyclerModel(R.drawable.k1))
        itemList.add(RecyclerModel(R.drawable.k2))
        itemList.add(RecyclerModel(R.drawable.k3))
        itemList.add(RecyclerModel(R.drawable.k4))
        itemList.add(RecyclerModel(R.drawable.k5))
        itemList.add(RecyclerModel(R.drawable.k6))
        itemList.add(RecyclerModel(R.drawable.b1))
        itemList.add(RecyclerModel(R.drawable.b2))
        itemList.add(RecyclerModel(R.drawable.b3))
        itemList.add(RecyclerModel(R.drawable.j1))
        itemList.add(RecyclerModel(R.drawable.j2))
        itemList.add(RecyclerModel(R.drawable.j3))
        itemList.add(RecyclerModel(R.drawable.j4))
        itemList.add(RecyclerModel(R.drawable.j5))
        itemList.add(RecyclerModel(R.drawable.j6))
        itemList.add(RecyclerModel(R.drawable.i1))
        itemList.add(RecyclerModel(R.drawable.i2))
        itemList.add(RecyclerModel(R.drawable.i3))

        val adapter = RecyclerHorizontalAdapter(itemList, this) // Pass 'this' as context
        binding1.rvPics.adapter = adapter
        binding1.rvPics.layoutManager = GridLayoutManager(this, 2)

        adapter.setItemClickListener(object : RecyclerHorizontalAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {


                Toast.makeText(
                    applicationContext,
                    " ${itemList[position].image}",
                    Toast.LENGTH_SHORT
                ).show()
            }


        })

    }
}