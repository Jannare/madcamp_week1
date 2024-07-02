package com.example.week1

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class GalleryData(
    val img: Uri?,
    val date: String,
)

class sharedViewModel : ViewModel() {
    val _datap = MutableLiveData<MutableList<GalleryData>>(mutableListOf())
    val datap: LiveData<MutableList<GalleryData>> get() = _datap

    fun addGalleryData(data: GalleryData) {
        _datap.value?.apply {
            add(data)
            _datap.value = this
        }
    }
}