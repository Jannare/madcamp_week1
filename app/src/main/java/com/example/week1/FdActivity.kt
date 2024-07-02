package com.example.week1

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.week1.databinding.ActivityFolderBinding
import com.example.week1.databinding.PhototimeBinding
import java.io.FileOutputStream
import java.text.SimpleDateFormat

class FdActivity : AppCompatActivity() {

    //필수 변수
    private lateinit var FdActivityAdapter: FdActivityAdapter
    private lateinit var binding: ActivityFolderBinding
    lateinit var GalleryAdapter: GalleryactivityAdapter //RV를 위해..
    private var dataf = mutableListOf<Fddata>()
    var datap = mutableListOf<GalleryData>()
    lateinit var binding1: PhototimeBinding //카메라를 위해..

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
        binding = ActivityFolderBinding.inflate(layoutInflater) // 폴더용
        binding1 = PhototimeBinding.inflate(layoutInflater) //카메라 촬영용
        setContentView(binding.root)
        binding.FdRecyclerview.addItemDecoration(VerticalItemDecorator(20))
        binding.FdRecyclerview.addItemDecoration(VerticalItemDecorator(10))

        FdActivityAdapter = FdActivityAdapter(this)
        FdActivityAdapter.dataf = dataf
        binding.FdRecyclerview.adapter = FdActivityAdapter
        binding.FdRecyclerview.layoutManager = GridLayoutManager(this,2,GridLayoutManager.HORIZONTAL, false)

        GalleryAdapter = GalleryactivityAdapter(datap,this)
        GalleryAdapter.datap = datap
        addDataToList()

        binding.change23Button.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }
        binding.change21Button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


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
                            val date = getImageDate(uri)
                            datap.add(GalleryData(img = uri, date = date.toString()))
                        }
//                        if (uri != null) {
//                            imageList.add(uri)
//                        }
                        imageView.setImageURI(uri)
                        GalleryAdapter.notifyDataSetChanged()
                    }
                }
                storage_code -> {
                    val img = data?.extras?.get("data") as Bitmap
                    val uri = data!!?.data
                    if (uri != null) {
                        val date = getImageDate(uri)
                        datap.add(GalleryData(img = uri, date = date.toString()))
                    }
//                    if (uri !=null)
//                        imageList.add(uri)
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
    //사진에서 날짜를 가져오는 함수
    fun getImageDate(imageUri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATE_TAKEN)
        val cursor: Cursor? = contentResolver.query(imageUri, projection, null, null, null)
        var date: String? = null

        cursor?.use {
            if (it.moveToFirst()) {
                val dateTakenColumnIndex = it.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)
                if (dateTakenColumnIndex != -1) {
                    val dateTakenMillis = it.getLong(dateTakenColumnIndex)
                    date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateTakenMillis)
                }
            }
        }
        return date
    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {

        //누른 코드 is not null
        if (it.resultCode == RESULT_OK) {

            if(it.data!!.clipData != null) { //다중이미지

                //num of img
                val count = it.data!!.clipData!!.itemCount

                for (index in 0 until count) {
                    //이미지 담기
                    val imageUri = it.data!!.clipData!!.getItemAt(index).uri
                    // 이미지 add
//                    imageList.add(imageUri)
                    // rv에 add
                    val date = getImageDate(imageUri)
                    datap.add(GalleryData(img = imageUri, date = date.toString()))
                    Toast.makeText(this, "Date: $date",Toast.LENGTH_LONG).show()
                }

            }else { //single image
                val imageUri = it.data!!.data
//                imageList.add(imageUri!!)
                val date = getImageDate(imageUri!!)
                datap.add(GalleryData(img = imageUri, date = date.toString()))
                Toast.makeText(this, "Date: $date",Toast.LENGTH_LONG).show()
            }
            //적용
            GalleryAdapter.notifyDataSetChanged()
        }
    }

    fun addDataToList() {
        dataf.add(Fddata(Folderpic = R.drawable.profile1, date = "1월" ))
        dataf.add(Fddata(Folderpic = R.drawable.profile2, date = "2월" ))
        dataf.add(Fddata(Folderpic = R.drawable.profile3, date = "3월" ))
    }
}