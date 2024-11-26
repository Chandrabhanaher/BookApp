package com.chandrabhan.bookapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chandrabhan.bookapp.models.Item
import com.chandrabhan.bookapp.models.repo.BookRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository = BookRepository.getInstance()) :
    ViewModel() {

    val bookMutableState: MutableState<List<Item>> = mutableStateOf(emptyList<Item>())
    val mealState = mutableStateOf<Item?>(null)
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    fun getBookList(bookName: String, author: String) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response = getBookItems(bookName, author)
            bookMutableState.value = response
        }
    }

    private suspend fun getBookItems(bookName: String, author: String): List<Item> {
        return repository.getBookItemsList(bookName, author).items
    }

    fun getBookDetails(id:String){
        mealState.value = repository.getItemDetails(id)
    }

}