package com.example.week1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week1.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var profileAdapter: ProfileAdapter
    val datas = mutableListOf<ProfileData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding


        initRecycler()

        val number = 11.01

        binding.change22Button.setOnClickListener {
            val intent = Intent(this, FdActivity::class.java)
            fun main() {
                println("엄창용 바보0")
            }
            main()
            startActivity(intent)

        }

        binding.change23Button.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra("number",number)
            fun main() {
                println("엄창용 바보1")
            }
            main()
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
        binding.rvProfile.addItemDecoration(VerticalItemDecorator(20))
        binding.rvProfile.addItemDecoration(HorizontalItemDecorator(10))

        datas.apply {
            add(ProfileData(img = R.drawable.profile1, name = "Seowon", bd = "2002.10.30"))
            add(ProfileData(img = R.drawable.profile3, name = "Suhwan", bd = "2000.04.24"))
            add(ProfileData(img = R.drawable.profile2, name = "Hyunji", bd = "2002.02.07"))
            add(ProfileData(img = R.drawable.profile5, name = "Gyeongsuk", bd = "2005.11.09"))
            add(ProfileData(img = R.drawable.profile4, name = "Taeseok", bd = "2000.07.03"))

            profileAdapter.datas = datas
            profileAdapter.notifyDataSetChanged()
        }
    }
}