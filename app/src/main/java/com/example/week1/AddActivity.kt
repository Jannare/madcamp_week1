package com.example.week1

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week1.databinding.ActivityAddBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var edtName: EditText
    private lateinit var edtNumber: EditText
    private lateinit var edtBd: EditText
    private lateinit var edtInsta: EditText
    private lateinit var btnEditImage: ImageButton
    private lateinit var btnSubmit: ImageButton

    lateinit var name: String
    lateinit var number: String
    lateinit var bd: String
    lateinit var insta: String
    var photo: Uri = Uri.parse(R.drawable.cake.toString())

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        //누른 코드 is not null
        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data
            if (imageUri != null) {
                photo = imageUri
                //contentResolver.takePersistableUriPermission(photo, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val bitmap = uriToBitmap(photo)
                if (bitmap != null) {
                    val drawable = BitmapDrawable(resources, bitmap)
                    btnEditImage.setImageDrawable(drawable)
                }
                Toast.makeText(this, "프로필 사진 설정 완료", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, com.example.week1.R.layout.activity_add) as ActivityAddBinding

        edtName = binding.edtName
        edtNumber = binding.edtNumber
        edtBd = binding.edtBd
        edtInsta = binding.edtInsta
        btnEditImage = binding.btnEdtImage
        btnSubmit = binding.btnSubmit
        binding.btnEdtImage.setBackgroundResource(R.drawable.plus)
        binding.btnSubmit.setBackgroundResource(R.drawable.savee)

        btnEditImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }

        btnSubmit.setOnClickListener {
            name = edtName.text.toString()
            number = edtNumber.text.toString()
            bd = edtBd.text.toString()
            insta = edtInsta.text.toString()
            val savedImageUri = saveImageToInternalStorage(uriToBitmap(photo))

            if (name.isNotEmpty() && number.isNotEmpty() && bd.isNotEmpty() && insta.isNotEmpty()) {
                if (savedImageUri != null) {
                    val resultIntent = Intent().apply {
                        putExtra("name", name)
                        putExtra("number", number)
                        putExtra("bd", bd)
                        putExtra("photo", savedImageUri.toString()) // 저장된 이미지 URI
                        putExtra("insta", insta)
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish()
                } else {
                    Toast.makeText(this, "Please add a photo :)", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this, "Fill in all the blank :)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (grant in grantResults){
            if(grant != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "저장소 권한을 승인해주세요", Toast.LENGTH_LONG).show()
            }
        }
    }

    // URI를 비트맵으로 변환하는 함수
    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)

            // 이미지 크기 조정
            val targetWidth = btnEditImage.width
            val targetHeight = btnEditImage.height
            Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, false)
        } catch (e: Exception) {
            e.printStackTrace()
            null
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
            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}