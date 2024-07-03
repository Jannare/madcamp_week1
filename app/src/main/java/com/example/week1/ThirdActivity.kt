package com.example.week1

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ThirdActivity : AppCompatActivity() {


    val storage = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    } else {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    }
    val storage_code = 99


    private lateinit var targetView: View
    private lateinit var currentView: EditText

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        fun checkPermission(permissions: Array<out String>, type: Int): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (permission in permissions) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            permission
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(this, permissions, type)
                        return false
                    }
                }
            }
            return true
        }





        targetView = findViewById(R.id.ll2save)
        currentView = findViewById(R.id.et2write)
        findViewById<Button>(R.id.bg_spring).setOnClickListener {
            targetView.setBackgroundResource(R.drawable.spring)
        }
        findViewById<Button>(R.id.bg_summer).setOnClickListener {
            targetView.setBackgroundResource(R.drawable.summer)
        }
        findViewById<Button>(R.id.bg_fall).setOnClickListener {
            targetView.setBackgroundResource(R.drawable.fall)
        }
        findViewById<Button>(R.id.bg_winter).setOnClickListener {
            targetView.setBackgroundResource(R.drawable.winter)
        }

        findViewById<Button>(R.id.ft_basic).setOnClickListener {
            applyCustomFontToSelection(currentView, R.font.basicft)
        }
        findViewById<Button>(R.id.ft_cute).setOnClickListener {
            applyCustomFontToSelection(currentView, R.font.cuteft)
        }
        findViewById<Button>(R.id.ft_polite).setOnClickListener {
            applyCustomFontToSelection(currentView, R.font.politeft)
        }
        findViewById<Button>(R.id.ft_free).setOnClickListener {
            applyCustomFontToSelection(currentView, R.font.freeft)
        }

        findViewById<Button>(R.id.tc_happy).setOnClickListener {
            currentView.append("(๑'ᵕ'๑)⸝*")
        }
        findViewById<Button>(R.id.tc_love).setOnClickListener {
            currentView.append("(۶•̀ᴗ•́)۶—̳͟͞͞♡")
        }
        findViewById<Button>(R.id.tc_fun).setOnClickListener {
            currentView.append("ƪ(˘⌣˘)┐")
        }
        findViewById<Button>(R.id.tc_cheers).setOnClickListener {
            currentView.append("♡⸜(ˆᗜˆ˵ )⸝♡")
        }
        findViewById<Button>(R.id.tc_wink).setOnClickListener {
            currentView.append("(•͈ᴗ-)ᓂ-ෆ")
        }
        findViewById<Button>(R.id.tc_shy).setOnClickListener {
            currentView.append("(*/ω＼*)")
        }
        findViewById<Button>(R.id.tc_sad).setOnClickListener {
            currentView.append("ʕ´•̥ᴥ•̥`ʔ")
        }
        findViewById<Button>(R.id.tc_sleep).setOnClickListener {
            currentView.append("_(:ι」∠)_")
        }

        fun getViewBitmap(view: View): Bitmap {
            val bitmap = Bitmap.createBitmap(
                view.width, view.height, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }

        fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri? {
            val filename = "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyApp") // "Pictures/MyApp" 경로에 저장
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
            }

            val contentResolver = context.contentResolver
            val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            if (imageUri != null) {
                try {
                    contentResolver.openOutputStream(imageUri)?.use { fos ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        contentValues.clear()
                        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                        contentResolver.update(imageUri, contentValues, null, null)
                    }
                    Log.d("FileSave", "Image saved to gallery: $filename")
                    return imageUri
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e("FileSave", "Error saving image to gallery", e)
                }
            } else {
                Log.e("FileSave", "Failed to create new MediaStore record.")
            }
            return null
        }




        findViewById<Button>(R.id.btn_saveLetter).setOnClickListener {
            storage
            if (checkPermission(storage, storage_code)) {
                val bitmap = getViewBitmap(targetView)
                val uri = saveBitmapToFile(this, bitmap)
                if (uri != null) {
                    Toast.makeText(this, "편지가 갤러리에 저장되었습니다, 어서 보내러 가세요 !!", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("FileSave", "Image saved to gallery: $uri")
                } else {
                    Toast.makeText(this, "다시 시도해주세요 ㅜㅅㅜ", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("FileSave", "Null to gallery: $uri")
                }
            } else {
                Toast.makeText(this, "권한을 승인해주세요 ㅜㅅㅜ", Toast.LENGTH_SHORT).show()
                Log.e("FileSave", "Permission Error")
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


        val change22Button: Button = findViewById(R.id.change22Button)
        val change21Button: Button = findViewById(R.id.change21Button)

        change22Button.setOnClickListener {
            val intent = Intent(this, FdActivity::class.java)
            startActivity(intent)
        }
        change21Button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun SetFileName(): String {
        return SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis())
    }

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


    private fun applyCustomFontToSelection(editText: EditText, font: Int) {
        val start = editText.selectionStart
        val end = editText.selectionEnd

        if (start == end) {
            return
        }

        val spannable = SpannableStringBuilder(editText.text)
        val typeface = ResourcesCompat.getFont(this, font)
        typeface?.let {
            val typefaceSpan = CustomTypefaceSpan(it)
            spannable.setSpan(typefaceSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        editText.setText(spannable)
        editText.setSelection(start, end)
    }

    private class CustomTypefaceSpan(private val newType: Typeface) :
        TypefaceSpan("") { //TypefaceSpan을 상속받아 커스텀 폰트를 적용하는 클래스
        override fun updateDrawState(textPaint: android.text.TextPaint) {
            applyCustomTypeFace(textPaint, newType)
        }

        override fun updateMeasureState(paint: android.text.TextPaint) {
            applyCustomTypeFace(paint, newType)
        }

        private fun applyCustomTypeFace(paint: android.text.TextPaint, tf: Typeface) {
            paint.typeface = tf
        }
    }
}




//
//    private fun getViewBitmap(view: View): Bitmap {
//        val bitmap = Bitmap.createBitmap(
//            view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888
//        )
//        val canvas = Canvas(bitmap)
//        view.draw(canvas)
//        return bitmap
//    }
//
//    private fun getSaveFilePathName(): String {
//        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
//        val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
//        return "$folder/$fileName.jpg"
//    }
//
//
//

//    private fun bitmapFileSave(bitmap: Bitmap, path: String) {
//        val fos: FileOutputStream
//        try{
//            fos = FileOutputStream(File(path))
//            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos)
//            fos.close()
//        }catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun viewSave(view: View) {
//        val bitmap = getViewBitmap(view)
//        val filePath = getSaveFilePathName()
//        bitmapFileSave(bitmap, filePath)
//        val file = File(filePath)
//        if (file.exists()) {
//            Log.d("FileSave", "File saved successfully: $filePath")
//            Toast.makeText(this,"편지가 저장되었습니다, 어서 보내러 가세요 !!", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this,"편지가 저장되지 않았습니다 ㅜㅅㅜ!!", Toast.LENGTH_SHORT).show()
//        }
//    }