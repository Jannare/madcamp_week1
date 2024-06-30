package com.example.week1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week1.databinding.ActivityGalleryBinding

class Galleryactivity : AppCompatActivity() {
    lateinit var binding: ActivityGalleryBinding

    lateinit var GalleryAdapter: GalleryactivityAdapter

    var imageList: ArrayList<Uri> = ArrayList()

    var position = 0 // 이미지 현재 위치



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.change23Button.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }

        binding.change21Button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Adapter와 데이터 연결
        GalleryAdapter = GalleryactivityAdapter(imageList, this)

        //RecyclerView, 어댑터와 연결
        binding.recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerview.adapter = GalleryAdapter



        //버튼 이벤트
        binding.galleryBtn.setOnClickListener {

            //갤러리 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            //다중 선택
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            activityResult.launch(intent)
        }
    } //onCreate

    //결과 가져오기

    private val activityResult:ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        //누른 코드 is not null
        if (it.resultCode == RESULT_OK) {

            if(it.data!!.clipData != null) { //다중이미지

                //num of img
                val count = it.data!!.clipData!!.itemCount

                for (index in 0 until count) {
                    //이미지 담기
                    val imageUri = it.data!!.clipData!!.getItemAt(index).uri
                    // 이미지 add
                    imageList.add(imageUri)

                }
            }else { //single image
                val imageUri = it.data!!.data
                imageList.add(imageUri!!)
            }

            //적용
            GalleryAdapter.notifyDataSetChanged()
        }
    }
}