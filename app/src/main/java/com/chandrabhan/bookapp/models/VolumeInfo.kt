package com.chandrabhan.bookapp.models

data class VolumeInfo(
    var authors: List<String>? = null,
    var averageRating: Double = 0.0,
    val description: String,
    var imageLinks: ImageLinks?= null,
    val pageCount: Int,
    var ratingsCount: Int = 0,
    var subtitle: String?= null,
    val title: String
)