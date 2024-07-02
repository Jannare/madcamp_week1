package com.example.week1

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import androidx.core.content.res.ResourcesCompat

class ThirdActivity : AppCompatActivity() {
    private lateinit var targetView: View
    private lateinit var currentView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        targetView = findViewById(R.id.ll2save)
        currentView = findViewById(R.id.et2write)

        findViewById<Button>(R.id.btn_saveLetter).setOnClickListener {
            viewSave(targetView)
        }

        findViewById<Button>(R.id.bg_spring).setOnClickListener {
            targetView.setBackgroundResource(R.drawable.profile1)
        }
        findViewById<Button>(R.id.bg_summer).setOnClickListener {
            targetView.setBackgroundResource(R.drawable.profile2)
        }
        findViewById<Button>(R.id.bg_fall).setOnClickListener {
            targetView.setBackgroundResource(R.drawable.profile3)
        }
        findViewById<Button>(R.id.bg_winter).setOnClickListener {
            targetView.setBackgroundResource(R.drawable.profile4)
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


        val change22Button: Button = findViewById(R.id.change22Button)
        val change21Button: Button = findViewById(R.id.change21Button)
        var number = 33.03

        change22Button.setOnClickListener {
            val intent = Intent(this, FdActivity::class.java)
            intent.putExtra("number",number)
            startActivity(intent)
        }
        change21Button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("number",number)
            startActivity(intent)
        }
        number = intent.getDoubleExtra("number",0.0)
        Toast.makeText(this, number.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun applyCustomFontToSelection (editText: EditText, font: Int) {
        val start = editText.selectionStart
        val end = editText.selectionEnd

        if (start==end) { return }

        val spannable = SpannableStringBuilder(editText.text)
        val typeface = ResourcesCompat.getFont(this,font)
        typeface?.let {
            val typefaceSpan = CustomTypefaceSpan(it)
            spannable.setSpan(typefaceSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        editText.setText(spannable)
        editText.setSelection(start,end)
    }

    private class CustomTypefaceSpan(private val newType: Typeface): TypefaceSpan("") { //TypefaceSpan을 상속받아 커스텀 폰트를 적용하는 클래스
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

    private fun getViewBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun getSaveFilePathName(): String {
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        return "$folder/$fileName.jpg"
    }

    private fun bitmapFileSave(bitmap: Bitmap, path: String) {
        val fos: FileOutputStream
        try{
            fos = FileOutputStream(File(path))
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos)
            fos.close()
        }catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun viewSave(view: View) {
        val bitmap = getViewBitmap(view)
        val filePath = getSaveFilePathName()
        bitmapFileSave(bitmap, filePath)
        Toast.makeText(this,"편지가 저장되었습니다, 어서 보내러 가세요 !!", Toast.LENGTH_SHORT).show()
    }
}