package com.chandrabhan.bookapp.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.chandrabhan.bookapp.AppBar
import com.chandrabhan.bookapp.BookApplication
import com.chandrabhan.bookapp.LockScreenOrientation
import com.chandrabhan.bookapp.OrientationConfig
import com.chandrabhan.bookapp.PopupBox
import com.chandrabhan.bookapp.findActivity
import com.chandrabhan.bookapp.ui.theme.BookAppTheme
import com.chandrabhan.bookapp.ui.theme.ButtonColor
import com.chandrabhan.bookapp.ui.theme.Shapes

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookAppTheme {
                BookApplication()
            }
        }
    }
}

@Composable
fun BookHomeScreen(navController: NavController?) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    var bookName by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }

    var showPopup by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = { AppBar(title = "Search A Book") },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Search Criteria",
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = bookName,
                    onValueChange = { b -> bookName = b },
                    label = { Text(text = "Enter book name") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                )

                Text(
                    text = "Author",
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = author,
                    onValueChange = { b -> author = b },
                    label = { Text(text = "Enter book author") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                )

                Button(
                    onClick = {
                        if(bookName.isNotEmpty()){
                            navController?.navigate("book_list/${bookName}/${author}")
                        }else{
                            showPopup = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = Shapes.extraSmall,
                    colors = ButtonColors(
                        containerColor = ButtonColor,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Submit", style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ))
                }
            }

            PopupBox(popupWidth = 200.dp, popupHeight = 200.dp, showPopup = showPopup,
                onClickOutside = {showPopup = false}, content = "Book name is mandatory")
        }

        }
    }




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookAppTheme {
        BookHomeScreen(null)
    }
}