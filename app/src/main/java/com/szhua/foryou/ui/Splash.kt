package com.szhua.foryou.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.szhua.foryou.common.Pages
import androidx.navigation.compose.navigate
import androidx.navigation.compose.popUpTo
import com.szhua.foryou.R
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


/**

@author  SZhua
Create at  2021/4/13
Description:
 */
@Composable
fun SplashScreen(navController: NavController){
    val  skipPage= fun (){
        navController.navigate(Pages.MAIN_PAGE){
            popUpTo(Pages.SPLASH){
                inclusive=true
            }
        }
    }

   Scaffold{
       val skip by rememberUpdatedState(newValue = skipPage)
       LaunchedEffect(true){
           delay(3000)
           skip()
       }
       Box(contentAlignment = Alignment.Center ,modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = getResource()),
                contentDescription = "s" , contentScale = ContentScale.Crop)
        }
    }
}


fun getResource() :Int{
     val images = listOf(
         R.mipmap.day1,
         R.mipmap.day2,
         R.mipmap.day3,
         R.mipmap.day4,
         R.mipmap.day5,
         R.mipmap.day6,
         R.mipmap.day7
     )
   val index=  Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    return  images[index-1]
}