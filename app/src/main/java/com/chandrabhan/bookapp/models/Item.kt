package com.chandrabhan.bookapp.models

data class Item(
    val id: String,
    val kind: String,
    val selfLink: String,
    val volumeInfo: VolumeInfo
)