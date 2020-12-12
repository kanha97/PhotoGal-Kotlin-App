package com.devkanhaiya.photoingal

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.devkanhaiya.photoingal.Fragment.CameraFragment
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    lateinit var uris: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        val uri = intent.getStringExtra(CameraFragment.EXTRA)


        Glide.with(this)
                .load(uri)
                .into(image_view)

    }

}