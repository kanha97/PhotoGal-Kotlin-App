package com.devkanhaiya.photoingal.Fragment

import android.app.Activity
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout.HORIZONTAL
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkanhaiya.photoingal.Model.ImageInfo
import com.devkanhaiya.photoingal.OnviewClick
import com.devkanhaiya.photoingal.PhotoAdapter
import com.devkanhaiya.photoingal.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GoogleFragment : Fragment(), OnviewClick {

    lateinit var recyclerViewHorizontal: RecyclerView
    val ImageLists = ArrayList<ImageInfo>()
    lateinit var adapters: PhotoAdapter
    lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v =  inflater.inflate(R.layout.fragment_google, container, false)
        recyclerViewHorizontal = v.findViewById(R.id.recyclerviewhorizontal)

        recyclerViewHorizontal.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        imageView = v.findViewById(R.id.image_views)

        if (ActivityCompat.checkSelfPermission(
                context as Activity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1112
            )
        } else {
            loadImages()
        }
        return v
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1112 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            loadImages()

        }
    }


    private fun loadImages() {
        GlobalScope.launch(Dispatchers.IO) {

            val imageProjection = arrayOf(
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media._ID
            )
            val imageSortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

            val cursor = context!!.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageProjection,
                null,
                null,
                imageSortOrder
            )
            cursor.use {
                it?.let {
                    val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                    val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                    val dateColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

                    while (it.moveToNext()) {
                        val id = it.getLong(idColumn)
                        val name = it.getString(nameColumn)
                        val size = it.getString(sizeColumn)
                        val date = it.getString(dateColumn)
                        val contentUri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id
                        )
                        // add the URI to the list
                        val imageinfo = ImageInfo(name, size, date, contentUri)
                        ImageLists.add(imageinfo)

                    }
                } ?: kotlin.run {
                    Log.e("CameraFragment", "Cursor is full!")
                }

            }

        }


        adapters = PhotoAdapter(ImageLists, this)
        recyclerViewHorizontal.adapter = adapters
    }


    override fun onItemClick(imageInfo: ImageInfo) {


        context?.let {
            Glide.with(it)
                .load(imageInfo.uri)
                .into(imageView)
        }
    }


}