package com.chandrabhan.bookapp.networkservice

import android.R.attr.value
import com.chandrabhan.bookapp.models.BookAPIResponse
import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query


interface APIService {

    @GET("books/v1/volumes")
    suspend fun getBookList(
        @Query("q") bookName: String,
    ): BookAPIResponse
}


