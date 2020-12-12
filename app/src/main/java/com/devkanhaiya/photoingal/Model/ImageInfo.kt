package com.devkanhaiya.photoingal.Model

import android.net.Uri

data class ImageInfo(
        val title:String?= null,
        val size:String? = null,
        val date:String? = null,
        val uri : Uri
)