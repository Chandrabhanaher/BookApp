package com.chandrabhan.bookapp.ui

import android.content.pm.ActivityInfo
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.chandrabhan.bookapp.AppBar
import com.chandrabhan.bookapp.LockScreenOrientation
import com.chandrabhan.bookapp.OrientationConfig
import com.chandrabhan.bookapp.RatingBar
import com.chandrabhan.bookapp.models.Item
import com.chandrabhan.bookapp.viewmodel.BookViewModel

@Composable
fun BookList(bookName: String, author: String,navCallback:(String)->Unit) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val viewModel: BookViewModel = viewModel()
    viewModel.getBookList(bookName, author)
    var loading by remember { mutableStateOf(true) }
    Scaffold(
        topBar = { AppBar(title = "Book List") },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime)) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
            OrientationConfig()
            val bookList = viewModel.bookMutableState.value
            if (bookList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp).size(100.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }

            } else {
                loading = false
                BookListScreen(bookList,navCallback)
            }
        }
    }
}

@Composable
fun BookListScreen(books: List<Item>, navCallback: (String) -> Unit) {
    LazyColumn {
        itemsIndexed(books) { index, book ->
            Spacer(modifier = Modifier.height(8.dp))
            ShowItems(book,navCallback)
            Spacer(modifier = Modifier.height(8.dp))
            if (index < books.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.5.dp,
                    color = Color.Gray,
                )
            }
        }
    }
}

@Composable
fun ShowItems(book: Item, navCallback: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {navCallback(book.id)},
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start) {
        book.volumeInfo.imageLinks?.smallThumbnail?.let {image->
            val thumbnail = image.replace("http", "https")
            Image(
                painter = rememberAsyncImagePainter(thumbnail),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp, 150.dp)
                    .align(Alignment.CenterVertically)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            Text(
                text = book.volumeInfo.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
            )
            book.volumeInfo.authors?.let {
                val str =
                    it.toString().replace("[\\[\\]]".toRegex(), "").replace(",".toRegex(), "and")
                Log.e("ITEMS_VAL", "Authors $str")
                Text(
                    text = str,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            RatingBar(
                modifier = Modifier.size(45.dp),
                rating = book.volumeInfo.averageRating
            )
        }
    }
}

@Composable
fun BookDetails(bookId: String) {
    val viewModel : BookViewModel = viewModel()
    viewModel.getBookDetails(bookId)
    Scaffold(
        topBar = { AppBar(title = "Book Details") },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime)) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val bookDetails =  viewModel.mealState.value!!
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                bookDetails.volumeInfo.imageLinks?.thumbnail?.let {image->
                    val thumbnail = image.replace("http", "https")
                    Image(
                        painter = rememberAsyncImagePainter(thumbnail),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp, 200.dp)
                    )
                }
                Text(text = bookDetails.volumeInfo.title,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        lineHeight = 28.sp,
                        letterSpacing = 0.sp
                    )
                )

                bookDetails.volumeInfo.subtitle?.let {subtitle->
                    Text(text = subtitle ,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium)
                }
                bookDetails.volumeInfo.authors?.let {author->
                    val str = author.toString().replace("[\\[\\]]".toRegex(), "").replace(",".toRegex(), "and")
                    Text(
                        text = "By $str",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
               Row(modifier = Modifier.fillMaxWidth(),
                   verticalAlignment = Alignment.CenterVertically,
                   horizontalArrangement = Arrangement.Start) {
                   RatingBar(
                       modifier = Modifier.size(45.dp),
                       rating = bookDetails.volumeInfo.averageRating
                   )
                   Text(
                       text = "(${bookDetails.volumeInfo.ratingsCount})",
                       style = MaterialTheme.typography.bodyMedium
                   )
               }

                Text(
                    text = "By ${bookDetails.volumeInfo.description}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookListPreview() {
   // BookList("", ""){}
}

@Preview(showBackground = true)
@Composable
fun BookDetailsPreview() {
    BookDetails("")
}