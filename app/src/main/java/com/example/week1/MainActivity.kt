package com.example.week1

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week1.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //이렇게 binding 해줬지만, 수정이 필요해져서 var 선언
    private lateinit var rrvv: RecyclerView
    private lateinit var ssvv: SearchView
    private lateinit var profileAdapter: ProfileAdapter
    private var datas = mutableListOf<ProfileData>()
    private var saveddata: MutableList<ProfileData> = mutableListOf()
    private lateinit var addActivityResultLauncher: ActivityResultLauncher<Intent>
    var isDataParsed = false



    override fun onPause() {
        super.onPause()
        saveData()
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("galleryData1", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        if(datas != null){
            saveddata = mutableListOf()
            saveddata.clear()
            for (i in datas.indices) {
                saveddata.add(
                    ProfileData(
                        name = datas[i].name,
                        bd = datas[i].bd,
                        img = datas[i].img,
                        snsData = datas[i].snsData
                    )
                )
            }
            editor.putString("gsonData1", gson.toJson(saveddata))
            editor.apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        datas.clear()
        super.onCreate(savedInstanceState)
        fun decodeFile(filePath: String): Bitmap? {
            // 파일 경로에서 Bitmap 로드
            return BitmapFactory.decodeFile(filePath)
        }

        //데이터 불러오기
        fun loadData() {
            val sharedPreferences = getSharedPreferences("galleryData1", MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("gsonData1", null)
            datas = mutableListOf()
            datas.clear()
            if (isDataParsed != true) {
                addDataToList()
                sharedPreferences.edit().remove(json)
                isDataParsed = true
            }
            if (json != null) {
                val type =
                    object : TypeToken<MutableList<ProfileData>>() {}.type
                val intedata: MutableList<ProfileData> =
                    gson.fromJson(json, type)
                val nameList: List<String> = intedata.map { it.name }
                Log.d("GalleryActivity", "Extracted nameList: $nameList")
                val bdList: List<String> = intedata.map { it.bd }
                Log.d("GalleryActivity", "Extracted date: $bdList")
                val imgList: List<String> = intedata.map { it.img }
                Log.d("GalleryActivity", "Extracted imgList: $imgList")
                val snsDataList: List<ProfileData.SnsData> = intedata.map { it.snsData }
                Log.d("GalleryActivity", "Extracted imgList: $snsDataList")
                datas.clear()
                saveddata.clear()
                saveddata = mutableListOf()
                for (i in imgList.indices) {
                    datas.add(ProfileData(name = nameList[i], bd = bdList[i], img = imgList[i], snsData = snsDataList[i]))
                }
//                addDataToList() //initrecycler에 있던 부분에서 data 관련된 것만 빼버림.
                ProfileAdapter(this).notifyDataSetChanged()

            }
        }
        //enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding
        binding.createProfileButton.setBackgroundResource(R.drawable.plus)


        rrvv = binding.rvProfile
        ssvv = binding.svProfile

        rrvv.setHasFixedSize(true)
        rrvv.layoutManager = LinearLayoutManager(this)

        loadData() // 데이터 불러오기

        profileAdapter = ProfileAdapter(this)
        profileAdapter.datas=datas
        rrvv.adapter = profileAdapter


        addActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { data ->
                    val name = data.getStringExtra("name") ?: return@let
                    val number = data.getStringExtra("number") ?: return@let
                    val bd = data.getStringExtra("bd") ?: return@let
                    val imgUriString = data.getStringExtra("photo") ?: return@let // 문자열로 받은 URI
                    val imgUri = Uri.parse(imgUriString) // 문자열을 다시 Uri로 변환
                    val img = imgUri.toString()
                    val imgicon = R.drawable.msgicon1 //일단은 이걸로 해놓기 -> 나중에 바꿀 거여
                    val insta = data.getStringExtra("insta") ?: return@let
                    val snsData = ProfileData.SnsData(imgicon, number, insta)
                    datas.add(ProfileData(name = name, bd = bd, img = img, snsData = snsData))
                    profileAdapter.notifyDataSetChanged()
                }
            }
        }

        binding.createProfileButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            addActivityResultLauncher.launch(intent)
        }

        binding.change22Button.setOnClickListener {
            val intent = Intent(this, FdActivity::class.java)
            startActivity(intent)
        }

        binding.change23Button.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
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

    fun getUriToDrawable(resId: Int): Uri {
        return Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(resId))
            .appendPath(resources.getResourceTypeName(resId))
            .appendPath(resources.getResourceEntryName(resId))
            .build()
    }

    private fun addDataToList() {
        datas.add(ProfileData(img = getUriToDrawable(R.drawable.profile1).toString(), name = "알버트 아인슈타인", bd = "1879.04.14", snsData = ProfileData.SnsData(imgIcon = R.drawable.callicon, number="010-1111-2222", insta= "shinseon_1030")))
        datas.add(ProfileData(img = getUriToDrawable(R.drawable.profile3).toString(), name = "아이작 뉴턴", bd = "1643.01.04", snsData = ProfileData.SnsData(imgIcon = R.drawable.callicon, number="010-2222-3333", insta= "k.__swan")))
        datas.add(ProfileData(img = getUriToDrawable(R.drawable.profile2).toString(), name = "존 폰 노이만", bd = "1903.12.28", snsData = ProfileData.SnsData(imgIcon = R.drawable.callicon, number="010-3333-4444", insta= "shinseon_1030")))
        datas.add(ProfileData(img = getUriToDrawable(R.drawable.profile5).toString(), name = "갈릴레오 갈릴레이", bd = "1564.02.15", snsData = ProfileData.SnsData(imgIcon = R.drawable.callicon, number="010-4444-5555", insta= "k.__swan")))
        datas.add(ProfileData(img = getUriToDrawable(R.drawable.profile4).toString(), name = "마리 퀴리", bd = "1867.11.07", snsData = ProfileData.SnsData(imgIcon = R.drawable.callicon, number="010-5555-6666", insta= "shinseon_1030")))
        datas.add(ProfileData(img = getUriToDrawable(R.drawable.profile1).toString(), name = "앨런 튜링", bd = "1912.06.23", snsData = ProfileData.SnsData(imgIcon = R.drawable.callicon, number="010-0404-0404", insta= "alan____turing")))
        datas.add(ProfileData(img = getUriToDrawable(R.drawable.profile3).toString(), name = "스티브 호킹", bd = "1942.01.08", snsData = ProfileData.SnsData(imgIcon = R.drawable.callicon, number="010-7777-8888", insta= "shinseon_1030")))
        datas.add(ProfileData(img = getUriToDrawable(R.drawable.profile2).toString(), name = "토마스 에디슨", bd = "1847.02.11", snsData = ProfileData.SnsData(imgIcon = R.drawable.callicon, number="010-8888-9999", insta= "k.__swan")))
    }
}
