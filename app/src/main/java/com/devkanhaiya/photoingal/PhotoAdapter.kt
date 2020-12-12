package com.devkanhaiya.photoingal

import android.renderscript.Long4
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkanhaiya.photoingal.Model.ImageInfo
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PhotoAdapter(val imagelist: ArrayList<ImageInfo> = ArrayList<ImageInfo>(), val listener: OnviewClick): RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image_view: ImageView = itemView.findViewById(R.id.image)
        val titles: TextView = itemView.findViewById(R.id.title)
        val sizes: TextView = itemView.findViewById(R.id.size)
        val dates: TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = PhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_camera, parent, false))
        view.image_view.setOnClickListener {
            listener.onItemClick(imagelist[view.adapterPosition])
        }
        return view
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val image = imagelist[position]

        holder.titles.text = image.title
        val simple = image.date?.toLong()
       val simpledate = simple?.let { getDate(it, "dd/MM/yyyy") }
        image.date
        holder.dates.text = simpledate
        holder.sizes.text = image.size
        Glide.with(holder.image_view.context)
                .asBitmap()
                .load(image.uri)
                .into(holder.image_view)
    }

    override fun getItemCount(): Int {
       return imagelist.size
    }
}
interface OnviewClick{
    fun onItemClick(imageInfo: ImageInfo)
}

fun getDate(milliSeconds: Long, dateFormat: String?): String? {
    // Create a DateFormatter object for displaying date in specified format.
    val formatter = SimpleDateFormat(dateFormat)

    // Create a calendar object that will convert the date and time value in milliseconds to date.
    val calendar: Calendar = Calendar.getInstance()
    calendar.setTimeInMillis(milliSeconds)
    return formatter.format(calendar.getTime())
}