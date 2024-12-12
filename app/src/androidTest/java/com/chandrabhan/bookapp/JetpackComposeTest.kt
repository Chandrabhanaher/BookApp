package com.chandrabhan.bookapp

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.chandrabhan.bookapp.networkservice.NetworkAPIService
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by Chandrabhan Haribhau Aher on 11/27/2024
 * chandrabhan99@gmail.com
 */
class JetpackComposeTest {
    @get:Rule
    val composeRule = createComposeRule()
    lateinit var navController: TestNavHostController
    val bookName = "harry"
    val author = "rowling"


    @Before
    fun setUpNavHost(){
        composeRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            BookApplication(navHostController = navController)
        }
    }

    @Test
    fun navArgumentTest(){
        composeRule.onNodeWithTag("BOOK_NAME").performTextInput(bookName)
        composeRule.onNodeWithTag("BOOK_AUTHOR").performTextInput(author)
        composeRule.onNodeWithText("Submit").performClick()

        val currentBackStackArgument = navController.currentBackStackEntry?.arguments
        val book_name = currentBackStackArgument?.getString("bookName")
        val authotr_name = currentBackStackArgument?.getString("author")

        Assert.assertEquals(book_name,bookName)
        Assert.assertEquals(authotr_name,author)

    }


    @Test
    fun testingCompose(){

        composeRule.onNodeWithTag("BOOK_NAME").performTextInput(bookName)
        composeRule.onNodeWithTag("BOOK_AUTHOR").performTextInput(author)
        composeRule.onNodeWithText("Submit").performClick()
        composeRule.onNodeWithTag("BOOK_NAME").assert(hasText(bookName))
        composeRule.onNodeWithTag("BOOK_AUTHOR").assert(hasText(author))

        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(route,"book_list/{bookName}/{author}")
    }
}