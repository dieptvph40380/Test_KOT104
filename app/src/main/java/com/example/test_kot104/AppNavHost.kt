package com.example.test_kot104

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.test_kot104.screen.Home

import com.example.test_kot104.screen.welcome

enum class ROUTE_SCREEN_NAME {
    WELCOME,
    HOME
}

@Composable
fun AppNavHost (navController : NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_SCREEN_NAME.WELCOME.name
    ) {
        composable(ROUTE_SCREEN_NAME.WELCOME.name) {
            welcome(navController)
        }
        composable(ROUTE_SCREEN_NAME.HOME.name) {
            Home()

        }
    }
}