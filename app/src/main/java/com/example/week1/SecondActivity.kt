package com.example.week1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val change21Button: Button = findViewById(R.id.change21Button)
        val change23Button: Button = findViewById(R.id.change23Button)
        var number = 22.02

        change21Button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("number",number)
            startActivity(intent)
        }
        change23Button.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra("number",number)
            startActivity(intent)
        }

        number = intent.getDoubleExtra("number",0.0)
        Toast.makeText(this, number.toString(), Toast.LENGTH_SHORT).show()
    }
}