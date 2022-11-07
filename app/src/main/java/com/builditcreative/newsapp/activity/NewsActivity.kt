package com.builditcreative.newsapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.builditcreative.newsapp.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        var titleText = findViewById<TextView>(R.id.title_text)
        var detailsText = findViewById<TextView>(R.id.details_text)
        var descriptionText = findViewById<TextView>(R.id.description_text)
        var imageView = findViewById<ShapeableImageView>(R.id.image_view)

        titleText.text = intent.getStringExtra("Title")
        detailsText.text = intent.getStringExtra("Detail")
        descriptionText.text = intent.getStringExtra("Description")
        Glide.with(this).load(intent.getStringExtra("Image")).into(imageView)

    }
}