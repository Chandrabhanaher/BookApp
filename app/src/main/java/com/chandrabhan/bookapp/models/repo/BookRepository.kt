package com.chandrabhan.bookapp.models.repo

import com.chandrabhan.bookapp.models.BookAPIResponse
import com.chandrabhan.bookapp.models.Item
import com.chandrabhan.bookapp.networkservice.NetworkAPIService

class BookRepository {

    private var itemList = listOf<Item>()
    suspend fun getBookItemsList(bookName: String, author: String): BookAPIResponse {
       val authorName =  if(author.isEmpty()){
           ""
       }else{
           "?inauthor=${author}"
       }
        val response = NetworkAPIService.apiService.getBookList("${bookName}${authorName}")
        itemList = response.items
        return response
    }

    fun getItemDetails(id: String):Item?{
        return itemList.firstOrNull { it.id == id}
    }

    companion object {
        @Volatile
        private var instance: BookRepository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: BookRepository().also { instance = it }
        }
    }

}