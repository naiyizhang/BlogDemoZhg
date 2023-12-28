package com.my.blog.myapplication

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.my.blog.myapplication.view.FolderView

class FoldViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fold_view)
        val foldView = findViewById<FolderView>(R.id.fold)
        foldView.bitmaps = (listOf(
            BitmapFactory.decodeResource(resources, R.drawable.img_little_logo),
            BitmapFactory.decodeResource(resources, R.drawable.img_1),
            BitmapFactory.decodeResource(resources, R.drawable.img_2),
            BitmapFactory.decodeResource(resources, R.drawable.img_3),
            BitmapFactory.decodeResource(resources, R.drawable.img_4)

        ))
    }
}