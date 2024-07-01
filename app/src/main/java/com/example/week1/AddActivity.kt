package com.example.week1

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week1.databinding.ActivityAddBinding
import java.util.Locale

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var edtName: EditText
    private lateinit var edtNumber: EditText
    private lateinit var edtBd: EditText
    private lateinit var edtInsta: EditText
    private lateinit var btnEditImage: Button
    private lateinit var btnSubmit: Button

    lateinit var name: String
    lateinit var number: String
    lateinit var bd: String
    lateinit var insta: String

    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, com.example.week1.R.layout.activity_add) as ActivityAddBinding

        edtName = binding.edtName
        edtNumber = binding.edtNumber
        edtBd = binding.edtBd
        edtInsta = binding.edtInsta
        btnEditImage = binding.btnEdtImage
        btnSubmit = binding.btnSubmit

        btnSubmit.setOnClickListener(View.OnClickListener {
            name = edtName.getText().toString()
            number = edtNumber.getText().toString()
            bd = edtBd.getText().toString()
            insta = edtInsta.getText().toString()

            if (name.isNotEmpty() && number.isNotEmpty() && bd.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("name", name)
                resultIntent.putExtra("number", number)
                resultIntent.putExtra("bd", bd)
                resultIntent.putExtra("insta", insta)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Invalid format, Please note the example", Toast.LENGTH_SHORT).show()
            }
        })
    }
}