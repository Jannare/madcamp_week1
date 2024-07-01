package com.example.week1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week1.databinding.ActivityMainBinding
import java.util.Locale



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //이렇게 binding 해줬지만, 수정이 필요해져서 var 선언
    private lateinit var rrvv: RecyclerView
    private lateinit var ssvv: SearchView
    private lateinit var profileAdapter: ProfileAdapter
    private var datas = mutableListOf<ProfileData>()

    private lateinit var addActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding

        rrvv = binding.rvProfile
        ssvv = binding.svProfile

        rrvv.setHasFixedSize(true)
        rrvv.layoutManager = LinearLayoutManager(this)
        rrvv.addItemDecoration(VerticalItemDecorator(20))
        rrvv.addItemDecoration(HorizontalItemDecorator(10))

        addDataToList() //initrecycler에 있던 부분에서 data 관련된 것만 빼버림.

        profileAdapter = ProfileAdapter(this)
        profileAdapter.datas=datas
        rrvv.adapter = profileAdapter

        val number = 11.01

        addActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let {
                    val name = it.getStringExtra("name") ?: return@let
                    //val number = it.getStringExtra("number") ?: return@let
                    val bd = it.getStringExtra("bd") ?: return@let
                    //val insta = it.getStringExtra("insta") ?: return@let
                    datas.add(ProfileData(name = name, bd = bd, img = R.drawable.k1))
                    //그리고 Profile안의 SNSData에도 접근해서 고칠 수 있게
                    //img는 버튼에서 받아온 걸로 되게.
                    profileAdapter.setFilteredList(datas)
                }
            }
        }

        binding.createProfileButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            addActivityResultLauncher.launch(intent)
        }

//        binding.createProfileButton.setOnClickListener {
//            val intent = Intent(this, AddActivity::class.java)
//            startActivity(intent)
//        }

        binding.change22Button.setOnClickListener {
            val intent = Intent(this, FdActivity::class.java)
            println("엄창용 바보0")
            startActivity(intent)
        }

        binding.change23Button.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra("number",number)
            println("엄창용 바보1")
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
                if (i.name.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT)) || i.bd.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))) {
                    filteredList.add(i) }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show()
            }
            else {
                profileAdapter.setFilteredList(filteredList)
            }
        }
    }

    private fun addDataToList() {
        datas.add(ProfileData(img = R.drawable.profile1, name = "Seowon", bd = "2002.10.30"))
        datas.add(ProfileData(img = R.drawable.profile3, name = "Suhwan", bd = "2000.04.24"))
        datas.add(ProfileData(img = R.drawable.profile2, name = "Hyunji", bd = "2002.02.07"))
        datas.add(ProfileData(img = R.drawable.profile5, name = "Gyeongsuk", bd = "2005.11.09"))
        datas.add(ProfileData(img = R.drawable.profile4, name = "Taeseok", bd = "2000.07.03"))
        datas.add(ProfileData(img = R.drawable.profile1, name = "신서원", bd = "2002.10.30"))
        datas.add(ProfileData(img = R.drawable.profile3, name = "김수환", bd = "2000.04.24"))
        datas.add(ProfileData(img = R.drawable.profile2, name = "Hyunji", bd = "2002.02.07"))
    }



}