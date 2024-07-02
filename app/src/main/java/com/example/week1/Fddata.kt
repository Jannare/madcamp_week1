package com.example.week1

import java.io.Serializable

data class Fddata(
    val Folderpic: Int,
    val date: String
) : Serializable {
    data class InteGalleryData (
        val img: String,
        val date: String
    ) : Serializable
}

