package com.example.week1

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week1.databinding.ActivityGalleryBinding
import com.example.week1.databinding.PhototimeBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.FileOutputStream
import java.text.SimpleDateFormat

class GalleryActivity : AppCompatActivity() {

    // 갤러리 불러오는데 필요한 변수
    lateinit var binding: ActivityGalleryBinding
    lateinit var galleryAdapter: GalleryActivityAdapter //RV를 위해..
    lateinit var binding1: PhototimeBinding //카메라를 위해..
    private var datap = mutableListOf<GalleryData>()
    private var intedata: MutableList<GalleryData.InteGalleryData> = mutableListOf()
    private var saveddata: MutableList<Fddata.InteGalleryData> = mutableListOf()
    var isDataParsed = false
//    private var imageList: ArrayList<Uri> = ArrayList()

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

    override fun onPause() {
        super.onPause()
        saveData()
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("galleryData2", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        if(datap != null){
            saveddata = mutableListOf()
            for (i in datap.indices) {
                saveddata.add(Fddata.InteGalleryData(img = datap[i].img.toString(), date = datap[i].date))
            }
            editor.putString("gsonData2", gson.toJson(saveddata))
            editor.apply()
        }
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("galleryData2", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("gsonData2", null)
        datap = mutableListOf()
        if (json != null) {
            val type =
                object : TypeToken<MutableList<GalleryData.InteGalleryData>>() {}.type
            val intedata: MutableList<GalleryData.InteGalleryData> =
                gson.fromJson(json, type)
            val imgList: List<String> = intedata.map { it.img }
            Log.d("GalleryActivity", "Extracted imgList: $imgList")
            val dateList: List<String> = intedata.map { it.date }
            Log.d("GalleryActivity", "Extracted date: $dateList")
            datap = mutableListOf()
            for (i in imgList.indices) {
                datap.add(GalleryData(img = (Uri.parse(imgList[i])), date = dateList[i]))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding1 = PhototimeBinding.inflate(layoutInflater) //카메라 촬영용
        binding = ActivityGalleryBinding.inflate(layoutInflater) // rv용
        setContentView(binding.root)
        binding.camBtn.setBackgroundResource(R.drawable.cam)
        binding.galleryBtn.setBackgroundResource(R.drawable.gal)



        galleryAdapter = GalleryActivityAdapter(this)
        binding.recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerview.adapter = galleryAdapter






        binding.change23Button.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }

        binding.change21Button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }



        //Adapter와 데이터 연결

        loadData()
        val img = intent.getStringExtra("PLEASE5")
        val date = intent.getStringExtra("PLEASED5")
        Log.d("GalleryActivity", "Intent0 received: $img, $date")
        if (img != null) {
            // img를 BitmapFactory.decodeFile(img)로 변환하는 부분
            //datap.add(GalleryData(img = (stringToBitmap(img)), date = date.toString()))
            datap.add(GalleryData(img = (Uri.parse(img)), date = date.toString()))
            galleryAdapter.notifyDataSetChanged()

        }

        val img1 = intent.getStringExtra("PLEASE1")
        val date1 = intent.getStringExtra("PLEASED1")
        Log.d("GalleryActivity", "Intent1 received: $img1, $date1")
        if (img1 != null) {
            // img를 BitmapFactory.decodeFile(img)로 변환하는 부분
            //datap.add(GalleryData(img = (stringToBitmap(img1)), date = date1.toString()))
            datap.add(GalleryData(img = (Uri.parse(img1)), date = date.toString()))
            galleryAdapter.notifyDataSetChanged()

        }

        val mergeddata = intent.getStringExtra("gsonData1")
        val gson = Gson()
        Log.d("GalleryActivity", "gsonData1 received: $mergeddata")
        if (mergeddata != null) {
            val intedata: MutableList<GalleryData.InteGalleryData> = gson.fromJson(mergeddata, object : TypeToken<MutableList<GalleryData.InteGalleryData>>() {}.type)
            val imgList: List<String> = intedata.map { it.img}
            Log.d("GalleryActivity", "Extracted imgList: $imgList")
            val dateList: List<String> = intedata.map { it.date }
            Log.d("GalleryActivity", "Extracted date: $dateList")
            for (i in imgList.indices) {
                datap.add(GalleryData(img = (Uri.parse(imgList[i])), date = dateList[i]))
            }
            galleryAdapter.notifyDataSetChanged()
        }

        val mergeddata2 = intent.getStringExtra("gsonData2")
        Log.d("GalleryActivity", "gsonData2 received: $mergeddata2")
        if (mergeddata2 != null) {
            val intedata: MutableList<GalleryData.InteGalleryData> = gson.fromJson(
                mergeddata2,
                object : TypeToken<MutableList<GalleryData.InteGalleryData>>() {}.type
            )
            val imgList: List<String> = intedata.map { it.img }
            Log.d("GalleryActivity", "Extracted imgList: $imgList")
            val dateList: List<String> = intedata.map { it.date }
            Log.d("GalleryActivity", "Extracted date: $dateList")
            for (i in imgList.indices) {
                datap.add(GalleryData(img = (Uri.parse(imgList[i])), date = dateList[i]))
            }
            galleryAdapter.notifyDataSetChanged()
        }
        val img3 = intent.getStringExtra("PLEASE3")
        val date3 = intent.getStringExtra("PLEASED3")
        Log.d("GalleryActivity", "Intent3 received: $img1, $date1")
        if (img3 != null) {
            // img를 BitmapFactory.decodeFile(img)로 변환하는 부분
            //datap.add(GalleryData(img = (stringToBitmap(img3)), date = date3.toString()))
            datap.add(GalleryData(img = (Uri.parse(img3)), date = date.toString()))
            galleryAdapter.notifyDataSetChanged()
        }
        galleryAdapter.datap = datap
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
                        permissionagain
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

        fun uriToBitmap(uri: Uri): Bitmap? {
            return try {
                val inputStream = contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        if(resultCode == Activity.RESULT_OK){

            when(requestCode){
                camera_code -> {
                    if(data?.extras?.get("data") != null){
                        val img = data?.extras?.get("data") as Bitmap
                        val uri = saveFile(SetFileName(), "image/jpeg", img)
                        if (uri != null) {
                            val date = getImageDate(uri)
                            val savedBit = uriToBitmap(uri)
                            datap.add(GalleryData(img = uri, date = date.toString()))
                            }
//                        if (uri != null) {
//                            imageList.add(uri)
//                        }
//                        imageView.setImageURI(uri)
                        galleryAdapter.notifyDataSetChanged()
                    }
                }
                storage_code -> {
                    val img = data?.extras?.get("data") as Bitmap
                    val uri = data!!?.data
                    if (uri != null) {
                        val date = getImageDate(uri)
                        val savedBit = uriToBitmap(uri)
                        datap.add(GalleryData(img = uri, date = date.toString()))
                    }
//                    if (uri !=null)
//                        imageList.add(uri)
//                    imageView.setImageURI(uri)
                    galleryAdapter.notifyDataSetChanged()
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

        fun uriToBitmap(uri: Uri): Bitmap? {
            return try {
                val inputStream = contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
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
                    val savedBit = uriToBitmap(imageUri)
                    datap.add(GalleryData(img = imageUri, date = date.toString()))
                    Toast.makeText(this, "Date: $date",Toast.LENGTH_LONG).show()
                }

            }else { //single image
                val imageUri = it.data!!.data
//                imageList.add(imageUri!!)
                val date = getImageDate(imageUri!!)
                val savedBit = uriToBitmap(imageUri)
                datap.add(GalleryData(img = imageUri, date = date.toString()))
                Toast.makeText(this, "Date: $date",Toast.LENGTH_LONG).show()
            }

            //적용
            galleryAdapter.notifyDataSetChanged()
        }
    }
    // 데이터에 추가하기
    private fun addData() {

    }
}