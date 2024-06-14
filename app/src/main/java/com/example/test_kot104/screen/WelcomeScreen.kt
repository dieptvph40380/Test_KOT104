package com.example.test_kot104.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavHostController
import com.example.test_kot104.R

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource


import com.example.test_kot104.ROUTE_SCREEN_NAME
import kotlinx.coroutines.delay


@Composable
fun welcome(navController: NavHostController){
    Column (
        Modifier
            .fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "")

        Text(text = "PH11324", style = MaterialTheme.typography.titleLarge)
        Button(onClick = { navController.navigate(ROUTE_SCREEN_NAME.HOME.name) }) {
            Text(text = "test")
        }
    }

//    LaunchedEffect(Unit) {
//        delay(2000L)
//        navController.navigate(ROUTE_SCREEN_NAME.HOME.name) {
//            popUpTo(ROUTE_SCREEN_NAME.WELCOME.name) { inclusive = true }
//        }
//    }
}