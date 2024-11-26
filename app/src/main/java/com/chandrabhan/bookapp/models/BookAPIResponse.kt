package com.chandrabhan.bookapp.models

data class BookAPIResponse(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)