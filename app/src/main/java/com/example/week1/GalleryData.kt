package com.example.week1

import android.net.Uri
import java.io.Serializable

data class GalleryData(
    val img: Uri?,
    val date: String,
) : Serializable