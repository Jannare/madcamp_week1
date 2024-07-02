package com.example.week1

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.week1.databinding.ActivityFolderBinding
import com.example.week1.databinding.PhototimeBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat

class FdActivity : AppCompatActivity() {
    private var datap = mutableListOf<GalleryData>()
    private lateinit var binding: ActivityFolderBinding
    private lateinit var galleryAdapter: GalleryActivityAdapter
    private lateinit var binding1: PhototimeBinding
    private var dataf = mutableListOf<Fddata>()
    private var intedata = mutableListOf<Fddata.InteGalleryData>()
    private lateinit var FdActivityAdapter: FdActivityAdapter
    private var saveddata: MutableList<Fddata.InteGalleryData> = mutableListOf()

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
            for (i in datap.indices) {
                saveddata.add(Fddata.InteGalleryData(img = datap[i].img.toString(), date = datap[i].date))
            }
            editor.putString("gsonData2", gson.toJson(saveddata))
            editor.apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        fun addDataToList() {
            dataf.add(Fddata(Folderpic = R.drawable.profile1, date = "모든 추억" ))
            dataf.add(Fddata(Folderpic = R.drawable.profile2, date = "1월의 추억" ))
            dataf.add(Fddata(Folderpic = R.drawable.profile3, date = "2월의 추억" ))
            dataf.add(Fddata(Folderpic = R.drawable.profile4, date = "3월의 추억" ))
            dataf.add(Fddata(Folderpic = R.drawable.profile3, date = "4월의 추억" ))
            dataf.add(Fddata(Folderpic = R.drawable.profile3, date = "5월의 추억" ))
            dataf.add(Fddata(Folderpic = R.drawable.profile3, date = "6월의 추억" ))
            dataf.add(Fddata(Folderpic = R.drawable.profile3, date = "3월" ))
        }
        addDataToList()

        super.onCreate(savedInstanceState)
        binding = ActivityFolderBinding.inflate(layoutInflater)
        binding1 = PhototimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FdActivityAdapter = FdActivityAdapter(this)
        FdActivityAdapter.dataf = dataf
        binding.FdRecyclerview.adapter = FdActivityAdapter
        binding.FdRecyclerview.layoutManager = GridLayoutManager(this,2,GridLayoutManager.HORIZONTAL, false)


        fun loadData() {
            val sharedPreferences = getSharedPreferences("galleryData2", MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("gsonData2", null)
            if (json != null) {
                val type = object : TypeToken<MutableList<GalleryData.InteGalleryData>>() {}.type
                val intedata: MutableList<GalleryData.InteGalleryData> = gson.fromJson(json, type)
                val imgList: List<String> = intedata.map { it.img}
                Log.d("GalleryActivity", "Extracted imgList: $imgList")
                val dateList: List<String> = intedata.map { it.date }
                Log.d("GalleryActivity", "Extracted date: $dateList")
                for (i in imgList.indices) {
                    datap.add(GalleryData(img = (Uri.parse(imgList[i])), date = dateList[i]))
                }
            }
        }



        galleryAdapter = GalleryActivityAdapter(this)
        galleryAdapter.datap = datap
        binding.change23Button.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }
        loadData() // 데이터 불러오기
        binding.change21Button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.galleryBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            activityResult.launch(intent)
        }
        binding.camBtn.setOnClickListener {
            CallCamera()
        }


    }



    private fun saveImageToInternalStorage(bitmap: Bitmap?): Uri? {
        if (bitmap == null) return null
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: FileOutputStream? = null
        return try {
            fos = openFileOutput(filename, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            val file = File(filesDir, filename)
            val uri = Uri.fromFile(file)
            Log.e("SaveImage","Image saved to internal storage: $uri")
            uri
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("SaveImage", "Error saving image to internal storage")
            null
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }






    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            camera_code -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "카메라 권한을 승인해주세요", Toast.LENGTH_LONG).show()
                        return
                    }
                }
                CallCamera()
            }
            storage_code -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "저장소 권한을 승인해주세요", Toast.LENGTH_LONG).show()
                        return
                    }
                }
                // Storage 권한 승인됨
            }
        }
    }

    fun checkPermission(permissions: Array<out String>, type: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, type)
                    return false
                }
            }
        }
        return true
    }

    fun CallCamera() {
        if (checkPermission(camera, camera_code)) {
            if (checkPermission(storage, storage_code)) {
                val cameraintent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraintent, camera_code)
            }
        }
    }

    fun saveFile(fileName: String, mimeType: String, bitmap: Bitmap): Uri? {
        val CV = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val MediaContentUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CV)
        MediaContentUri?.let { uri ->
            contentResolver.openFileDescriptor(uri, "w")?.use {
                FileOutputStream(it.fileDescriptor).use { fos ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                CV.clear()
                CV.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(uri, CV, null, null)
            }
        }
        return MediaContentUri
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val imageView = binding1.getPhoto

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                camera_code -> {
                    (data?.extras?.get("data") as? Bitmap)?.let { img ->
                        saveFile(SetFileName(), "image/jpeg", img)?.let { uri ->
                            getImageDate(uri)?.let { date ->
                                val savedUri = saveImageToInternalStorage(uriToBitmap(uri))
                                if (savedUri != null) {
                                    val date = getImageDate(savedUri)
                                    val SavedString = savedUri.toString()
                                    val DateString = date.toString()
                                    Log.d("ActivityResult", "Camera Camera ok: $SavedString, date: $DateString")
                                    val GG = Intent(this, GalleryActivity::class.java).apply {putExtra("PLEASE5", SavedString) }.apply {putExtra("PLEASED5", DateString)}.apply {Log.d("ActivityResult", "Camera Camera Sending uri: $SavedString, date: $DateString")}
                                    println("${SavedString.javaClass}")
                                    println("${DateString.javaClass}")
                                    startActivity(GG)
                                }

                            }
                            imageView.setImageURI(uri)
                        }

                    }
                }
                storage_code -> {
                    (data?.data as? Uri)?.let { uri ->
                        getImageDate(uri)?.let { date ->
                            val savedUri = saveImageToInternalStorage(uriToBitmap(uri))
                            val savedBit = uriToBitmap(savedUri!!)
                            if (savedUri != null) {
                                val date = getImageDate(uri)
                                val SavedString = savedUri.toString()
                                val DateString = date.toString()
                                Log.d("ActivityResult", "Camera storage ok: $SavedString, date: $DateString")
                                val GG = Intent(this, GalleryActivity::class.java).apply {putExtra("PLEASE1", SavedString) }.apply {putExtra("PLEASED1", DateString)}.apply {Log.d("ActivityResult", "Camera Storage Sending uri: $SavedString, date: $DateString")}
                                println("${SavedString.javaClass}")
                                println("${DateString.javaClass}")
                                startActivity(GG)
                            }
                        }
                    }
                }
            }

        }
    }

    fun SetFileName(): String {
        return SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis())
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
    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {

            if (it.data!!.clipData != null) { //다중이미지

                //num of img
                val count = it.data!!.clipData!!.itemCount

                for (index in 0 until count) {
                    //이미지 담기
                    val imageUri = it.data!!.clipData!!.getItemAt(index).uri
                    Log.d("ActivityResult", "Multi 1 Uri: $imageUri")
                    val date = getImageDate(imageUri)
                    val savedUri = saveImageToInternalStorage(uriToBitmap(imageUri))
                    val SavedString = savedUri.toString()
                    val DateString = date.toString()
                    Log.d("ActivityResult", "Multi 2 ok: $SavedString, date: $DateString")
                    intedata.add(Fddata.InteGalleryData(img = SavedString, date = DateString))
                    val gson = Gson()
                    val GG = Intent(this, GalleryActivity::class.java).apply {
                        putExtra(
                            "gsonData1",
                            gson.toJson(intedata)
                        )
                    }.apply { Log.d("ActivityResult", "Multi Sending data String: $intedata") }
                    println("${SavedString.javaClass}")
                    println("${DateString.javaClass}")
                    startActivity(GG)
                }
            }

            } else { //single image
                val imageUri = it.data!!.data
                Log.d("ActivityResult", "Single 1 ok: $imageUri")
                val date = getImageDate(imageUri!!)
                val savedUri = saveImageToInternalStorage(uriToBitmap(imageUri))
                Log.d("ActivityResult", "Single 2 ok: $savedUri, date: $date")
                val SavedString = savedUri.toString()
                val DateString = date.toString()
                val GG = Intent(this, GalleryActivity::class.java).apply {putExtra("PLEASE3", SavedString) }.apply {putExtra("PLEASED3", DateString)}.apply {Log.d("ActivityResult", "Single Sending uri: $SavedString, date: $DateString")}
                println("${SavedString.javaClass}")
                println("${DateString.javaClass}")
                startActivity(GG)

        }
    }
}
