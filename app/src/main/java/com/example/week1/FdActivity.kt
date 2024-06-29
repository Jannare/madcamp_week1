package com.example.week1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.week1.databinding.ActivityFolderBinding
import com.example.week1.databinding.ActivityMainBinding

class FdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFolderBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_folder) as ActivityFolderBinding

        super.onCreate(savedInstanceState)

        val change23Button: Button = findViewById(R.id.change23Button)
        val change21Button: Button = findViewById(R.id.change21Button)
        var number = 33.03

        binding.folder1.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        change23Button.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
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