package com.example.week1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FolderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder)

        val change22Button: Button = findViewById(R.id.change22Button)
        val change21Button: Button = findViewById(R.id.change21Button)
        var number = 33.03

        change22Button.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
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
}