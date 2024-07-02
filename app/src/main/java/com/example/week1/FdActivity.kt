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
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.week1.databinding.ActivityFolderBinding
import com.example.week1.databinding.PhototimeBinding
import com.google.gson.Gson
import java.io.FileOutputStream
import java.text.SimpleDateFormat

class FdActivity : AppCompatActivity() {
    private var datap = mutableListOf<GalleryData>()
    private lateinit var binding: ActivityFolderBinding
    private val sharedViewModel: sharedViewModel by viewModels()
    private lateinit var galleryAdapter: GalleryactivityAdapter
    private lateinit var binding1: PhototimeBinding
    private var dataf = mutableListOf<Fddata>()
    private lateinit var FdActivityAdapter: FdActivityAdapter

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
        binding = ActivityFolderBinding.inflate(layoutInflater)
        binding1 = PhototimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FdActivityAdapter = FdActivityAdapter(this)
        FdActivityAdapter.dataf = dataf
        binding.FdRecyclerview.adapter = FdActivityAdapter
        binding.FdRecyclerview.layoutManager = GridLayoutManager(this,2,GridLayoutManager.HORIZONTAL, false)


        galleryAdapter = GalleryactivityAdapter(sharedViewModel.datap.value ?: mutableListOf(), this)

        binding.change23Button.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }
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
        addDataToList()
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
                                sharedViewModel.addGalleryData(GalleryData(img = uri, date = date))
                            }
                            imageView.setImageURI(uri)
                        }

                    }
                }
                storage_code -> {
                    (data?.data as? Uri)?.let { uri ->
                        getImageDate(uri)?.let { date ->
                            sharedViewModel.addGalleryData(GalleryData(img = uri, date = date))
                        }
                        imageView.setImageURI(uri)
                    }
                }
            }
            val galleryintent = Intent(this, Galleryactivity::class.java)
            startActivity(galleryintent)
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


    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
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
            galleryAdapter.notifyDataSetChanged()
            val gson = Gson()
            val galleryintent = Intent(this, Galleryactivity::class.java).apply{
                putExtra("galData", gson.toJson(datap))
            }
            galleryintent
        }
    }
    fun addDataToList() {
        dataf.add(Fddata(Folderpic = R.drawable.profile1, date = "1월" ))
        dataf.add(Fddata(Folderpic = R.drawable.profile2, date = "2월" ))
        dataf.add(Fddata(Folderpic = R.drawable.profile3, date = "3월" ))
    }
}
