package com.example.week1

import android.net.Uri
import java.io.Serializable

data class ProfileData(
    val name: String,
    val bd: String,
    val img: String, // Uri를 Str으로 저장
    val snsData: SnsData
) : Serializable {
    // Uri를 반환하는 메서드 추가
    fun getImgUri(): Uri {
        return Uri.parse(img)
    }
    //ProfileData와 SnsData 합치기
    data class SnsData(
        val imgIcon : Int,
        val number : String,
        val insta : String
    ) : Serializable
}