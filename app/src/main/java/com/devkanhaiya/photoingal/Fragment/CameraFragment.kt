package com.devkanhaiya.photoingal.Fragment

import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devkanhaiya.photoingal.ImageActivity
import com.devkanhaiya.photoingal.Model.ImageInfo
import com.devkanhaiya.photoingal.OnviewClick
import com.devkanhaiya.photoingal.PhotoAdapter
import com.devkanhaiya.photoingal.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CameraFragment : Fragment(), OnviewClick {
companion object{
    val EXTRA = "uri"
}
    lateinit var recyclerView: RecyclerView
    val ImageList = ArrayList<ImageInfo>()
    lateinit var adapter: PhotoAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_camera, container, false)

        recyclerView = v.findViewById(R.id.recycler_view)



        if (ActivityCompat.checkSelfPermission(
                context as Activity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 111
            )
        } else {
            loadImage()
        }

        recyclerView.layoutManager = GridLayoutManager(context,3)

        return v
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            loadImage()

        }
    }

    private fun loadImage() {

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
                        ImageList.add(imageinfo)

                    }
                } ?: kotlin.run {
                    Log.e("CameraFragment", "Cursor is full!")
                }

            }

        }


        adapter = PhotoAdapter(ImageList, this)
        recyclerView.adapter = adapter

    }





    override fun onItemClick(imageInfo: ImageInfo) {
        val image = imageInfo.uri
        val intent = Intent(context,ImageActivity::class.java)
        intent.putExtra(EXTRA,image.toString())
        startActivity(intent)
    }




}

