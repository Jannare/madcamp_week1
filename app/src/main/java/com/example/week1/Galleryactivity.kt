package com.example.week1

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week1.databinding.ActivityGalleryBinding
import com.example.week1.databinding.PhototimeBinding
import java.io.FileOutputStream
import java.text.SimpleDateFormat

class Galleryactivity : AppCompatActivity() {

    // 갤러리 불러오는데 필요한 변수
    lateinit var binding: ActivityGalleryBinding
    lateinit var GalleryAdapter: GalleryactivityAdapter
    lateinit var binding1: PhototimeBinding
    var imageList: ArrayList<Uri> = ArrayList()
    var position = 0 // 이미지 현재 위치

    // 사진 촬영에 필요한 변수, storage 권한 처리...
    val camera = arrayOf(Manifest.permission.CAMERA)
    val storage = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    } else {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    }
    val camera_code = 98
    val storage_code = 99


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding1 = PhototimeBinding.inflate(layoutInflater)
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
        binding.recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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
        //카메라 버튼 이벤트
        binding.camBtn.setOnClickListener {
            CallCamera() // 추후 추가할 fun
        }
    } //onCreate

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            camera_code -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "카메라 권한을 승인해주세요", Toast.LENGTH_LONG).show()

                        //재승인 요청
                        var permissionagain = Manifest.permission.CAMERA
                        }
                    }
                }

            storage_code -> {
                for (grant in grantResults){
                    if(grant != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "저장소 권한을 승인해주세요", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    //다른 권한 확인
    fun checkPermission(permissions: Array<out String>, type:Int):Boolean{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, type)
                    return false
                }
            }
        }
        return true
    }

    // 카메라 촬영 - 권한 처리
    fun CallCamera() {
        if(checkPermission(camera, camera_code)) {
            if ( checkPermission(storage, storage_code)) {
                val cameraintent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraintent, camera_code)

            }
        }

    }


    // 사진 저장
    fun saveFile(fileName:String, mimeType:String, bitmap: Bitmap): Uri?{

        var CV = ContentValues ()

        // MediaStore 파일 명 mime(media)Type 지정.
        CV.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        CV.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        //안정성 검사 업데이트 전까진 꼼짝 마!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            CV.put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        // MediaStore 에 파일을 저장
        val MediaContentUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CV)
        if(MediaContentUri != null) {
            var scriptor = contentResolver.openFileDescriptor(MediaContentUri, "w")

            val FOS = FileOutputStream(scriptor?.fileDescriptor)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FOS)
            FOS.close()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                CV.clear()
                //IS_PENDING 초기화
                CV.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(MediaContentUri, CV, null, null)
            }

        }
        return MediaContentUri

    }



    // 결과
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val imageView = binding1.getPhoto

        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                camera_code -> {
                    if(data?.extras?.get("data") != null){
                        val img = data?.extras?.get("data") as Bitmap
                        val uri = saveFile(SetFileName(), "image/jpeg", img)
                        if (uri != null) {
                            imageList.add(uri)
                        }
                        imageView.setImageURI(uri)
                        GalleryAdapter.notifyDataSetChanged()
                    }
                }
                storage_code -> {
                    val uri = data?.data
                    if (uri !=null)
                        imageList.add(uri)
                    imageView.setImageURI(uri)
                    GalleryAdapter.notifyDataSetChanged()
                }
            }
        }
    }
    //파일명 > 날짜로 저장 함수 SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis())
    fun SetFileName():String{
        val fileName = SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return fileName

    }

    // 갤러리 취득
//    fun getAlbum() {
//        if(checkPermission(storage, storage_code)){
//            val itt = Intent(Intent.ACTION_PICK)
//            itt.type = MediaStore.Images.Media.CONTENT_TYPE
//            activityResult.launch(itt)
//        }
//    }





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