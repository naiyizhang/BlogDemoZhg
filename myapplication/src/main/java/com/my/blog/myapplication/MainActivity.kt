package com.my.blog.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.foldview).setOnClickListener {
            startActivity(Intent(this, FoldViewActivity::class.java))
        }

        findViewById<Button>(R.id.erase).setOnClickListener {
            startActivity(Intent(this, EraseActivity::class.java))
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            startActivity(Intent(this,TestDrawableActivity::class.java))
        }
    }
}