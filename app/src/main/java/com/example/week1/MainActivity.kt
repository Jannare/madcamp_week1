package com.example.week1

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week1.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //이렇게 binding 해줬기 때문에 굳이 recyclerView랑 searchView 안 불러도 될 것 같은데 만약에 그걸 var로 설정해서 수정을 해야 하면 유투브처럼 하기.
    private lateinit var rrvv: RecyclerView
    private lateinit var ssvv: SearchView
    private lateinit var profileAdapter: ProfileAdapter
    private var datas = mutableListOf<ProfileData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding

        rrvv = binding.rvProfile
        ssvv = findViewById(R.id.sv_profile)

        rrvv.setHasFixedSize(true)
        rrvv.layoutManager = LinearLayoutManager(this)

        addDataToList()

        profileAdapter = ProfileAdapter(this)
        profileAdapter.datas=datas
        rrvv.adapter = profileAdapter

        // rrvv.addItemDecoration(VerticalItemDecorator(20))
        // rrvv.addItemDecoration(HorizontalItemDecorator(10))

        val number = 11.01

        binding.change22Button.setOnClickListener {
            val intent = Intent(this, FdActivity::class.java)
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
        ssvv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<ProfileData>()
            for (i in datas) {
                if (i.name.lowercase(Locale.ROOT).contains(query) || i.bd.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                profileAdapter.datas = filteredList
                profileAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun addDataToList() {
        datas.add(ProfileData(img = R.drawable.profile1, name = "Seowon", bd = "2002.10.30"))
        datas.add(ProfileData(img = R.drawable.profile3, name = "Suhwan", bd = "2000.04.24"))
        datas.add(ProfileData(img = R.drawable.profile2, name = "Hyunji", bd = "2002.02.07"))
        datas.add(ProfileData(img = R.drawable.profile5, name = "Gyeongsuk", bd = "2005.11.09"))
        datas.add(ProfileData(img = R.drawable.profile4, name = "Taeseok", bd = "2000.07.03"))
        datas.add(ProfileData(img = R.drawable.profile1, name = "Seowon", bd = "2002.10.30"))
        datas.add(ProfileData(img = R.drawable.profile3, name = "Suhwan", bd = "2000.04.24"))
        datas.add(ProfileData(img = R.drawable.profile2, name = "Hyunji", bd = "2002.02.07"))
    }
}