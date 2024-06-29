package com.example.week1

import java.io.Serializable

data class ProfileData(
    val name: String,
    val bd: String,
    val img: Int // 예시로 Int 형식의 이미지 리소스 ID를 사용
) : Serializable