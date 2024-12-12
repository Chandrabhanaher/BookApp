package com.chandrabhan.bookapp

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.StarHalf
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.chandrabhan.bookapp.ui.BookDetails
import com.chandrabhan.bookapp.ui.BookHomeScreen
import com.chandrabhan.bookapp.ui.BookList
import com.chandrabhan.bookapp.ui.theme.Shapes
import com.chandrabhan.bookapp.ui.theme.StarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            Text(text = title)
        }
    )
}

@Composable
fun BookApplication(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "search_screen") {
        composable(route = "search_screen") {
            BookHomeScreen(navHostController)
        }
        composable(
            route = "book_list/{bookName}/{author}",
            arguments = listOf(
                navArgument("bookName") { type = NavType.StringType },
                navArgument("author") { type = NavType.StringType }
            )){ backStackEntry ->
            val bookName = backStackEntry.arguments?.getString("bookName")
            val author = backStackEntry.arguments?.getString("author")
            //  val viewModel : BookViewModel = viewModel()
            BookList(bookName.toString(), author.toString()){bookId->
                navHostController.navigate("book_details/$bookId")
            }
        }
        composable(route = "book_details/{book_id}",
            arguments = listOf(navArgument("book_id") {
                type = NavType.StringType
            })) {
            val bookId = it.arguments?.getString("book_id")
            BookDetails(bookId!!)
        }
    }
}

fun isValidText(input: String): Boolean {
    return input.matches(Regex("[a-zA-Z]+"))
}


@Composable
fun PopupBox(
    popupWidth: Dp,
    popupHeight: Dp,
    showPopup: Boolean,
    onClickOutside: () -> Unit,
    content: String
) {
    if (showPopup) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .background(color = MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(10.dp))
                .border(1.dp, color = Companion.Black, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Popup(
                alignment = Alignment.Center,
                properties = PopupProperties(
                    clippingEnabled = true,
                    dismissOnClickOutside = true
                ),
                onDismissRequest = { onClickOutside() }) {
                Box(
                    modifier = Modifier
                        .size(width = popupWidth, height = popupHeight)
                        .clickable { onClickOutside() }
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = Shapes.extraSmall
                        )
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = MaterialTheme.shapes.extraSmall
                        ),
                ) {
                    Text(
                        text = content,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }

            }
        }
    }

}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 4,
    starsColor: Color = StarColor
) {

    var isHalfStar = (rating % 1) != 0.0

    Row {
        for (index in 1..stars) {
            Icon(
                imageVector =
                if (index <= rating)
                    Icons.Rounded.Star
                else
                    if (isHalfStar) {
                        isHalfStar = false
                        Icons.AutoMirrored.Rounded.StarHalf
                    } else {
                        Icons.Rounded.StarOutline
                    },
                contentDescription = null,
                tint = starsColor,
                modifier = modifier
            )
        }
    }
}


@Composable
fun OrientationConfig(){
    val configuration = LocalConfiguration.current
    when(configuration.orientation){
        Configuration.ORIENTATION_LANDSCAPE ->{
            configuration.orientation = Configuration.ORIENTATION_PORTRAIT
        }
        else ->{
            Log.e("orientation", "I PORTRAIT orientation")
        }
    }
}
@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }
}
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}




