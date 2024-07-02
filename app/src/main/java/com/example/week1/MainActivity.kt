package com.example.week1

import android.content.ContentResolver
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import android.net.Uri
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
                result.data?.let { data ->
                    val name = data.getStringExtra("name") ?: return@let
                    val number = data.getStringExtra("number") ?: return@let
                    val bd = data.getStringExtra("bd") ?: return@let
                    val imgUriString = data.getStringExtra("photo") ?: return@let // 문자열로 받은 URI
                    val imgUri = Uri.parse(imgUriString) // 문자열을 다시 Uri로 변환
                    val img = imgUri.toString()
                    val imgicon = R.drawable.msgicon //일단은 이걸로 해놓기 -> 나중에 바꿀 거여
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
        datas.add(ProfileData(img = getUriToDrawable(R.drawable.profile1).toString(), name = "Seowon", bd = "2002.10.30", snsData = ProfileData.SnsData(imgIcon = R.drawable.callicon, number="010-2923-4581", insta= "shinseon_1030")))
        datas.add(ProfileData(img = getUriToDrawable(R.drawable.profile3).toString(), name = "Suhwan", bd = "2000.04.24", snsData = ProfileData.SnsData(imgIcon = R.drawable.callicon, number="010-6472-8727", insta= "k.__swan")))
        datas.add(ProfileData(img = getUriToDrawable(R.drawable.profile2).toString(), name = "Hyunji", bd = "2002.02.07", snsData = ProfileData.SnsData(imgIcon = R.drawable.callicon, number="010-6468-5953", insta= "abbyycha_")))
        datas.add(ProfileData(img = getUriToDrawable(R.drawable.profile5).toString(), name = "Gyeongsuk", bd = "2005.11.09", snsData = ProfileData.SnsData(imgIcon = R.drawable.callicon, number="010-4082-8427", insta= "s.g.seogi")))
        datas.add(ProfileData(img = getUriToDrawable(R.drawable.profile4).toString(), name = "Hanjeong", bd = "2000.07.21", snsData = ProfileData.SnsData(imgIcon = R.drawable.callicon, number="010-6289-4581", insta= "yeon_u_68")))
        datas.add(ProfileData(img = getUriToDrawable(R.drawable.profile1).toString(), name = "Chaeun", bd = "2002.07.03", snsData = ProfileData.SnsData(imgIcon = R.drawable.callicon, number="010-4717-7215", insta= "chh_0703")))
        datas.add(ProfileData(img = getUriToDrawable(R.drawable.profile3).toString(), name = "Minji", bd = "1999.08.21", snsData = ProfileData.SnsData(imgIcon = R.drawable.callicon, number="010-6289-4581", insta= "hae.won.__")))
        datas.add(ProfileData(img = getUriToDrawable(R.drawable.profile2).toString(), name = "Sieun", bd = "2002.04.06", snsData = ProfileData.SnsData(imgIcon = R.drawable.callicon, number="010-4601-8412", insta= "ruozey")))
    }
}