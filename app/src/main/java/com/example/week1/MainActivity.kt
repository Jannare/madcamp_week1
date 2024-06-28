package com.example.week1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //var number: TextView = findViewById(R.id.number)
        //var clickbutton: Button = findViewById(R.id.clickbutton)
        //var editText: EditText = findViewById(R.id.editText)
        //var checkButton: Button = findViewById(R.id.checkButton)
        val change22Button: Button = findViewById(R.id.change22Button)
        val change23Button: Button = findViewById(R.id.change23Button)
        val number = 11.01

        change22Button.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("number",number)
            startActivity(intent)
        }
        change23Button.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra("number",number)
            startActivity(intent)
        }
        //clickbutton.setOnClickListener { number.text = "2" }
        //checkButton.setOnClickListener {
            //val insertText: String = editText.text.toString()
            //Toast.makeText(this, insertText, Toast.LENGTH_SHORT).show()
        //}
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

    }
}