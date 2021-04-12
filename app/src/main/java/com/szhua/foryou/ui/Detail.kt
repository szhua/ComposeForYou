package com.szhua.foryou.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.szhua.foryou.data.BMobDiary
import com.szhua.foryou.ui.theme.titleTextStyle

/**
@author  SZhua
Create at  2021/4/7
Description: DetailPage；
 */
@Composable
fun DetailScreen(diary: BMobDiary,nav: NavHostController){
     Scaffold(topBar = {
         TopAppBar(
             title = {
                 Text(text = "Detail", style = titleTextStyle, color = Color.White)
             },
             navigationIcon = {
                 IconButton(onClick = {
                     nav.popBackStack()
                     //nav.popBackStack()
                 }) {
                     Icon(Icons.Default.ArrowBackIos, "this is icon more", tint = Color.White)
                 }
             },
         )
     }) {
         Detail(diary = diary )
     }
}


/**
@author  SZhua
Create at  2021/4/7
Description: Detail Content；
 */
@Composable
fun Detail( diary: BMobDiary , hasMaxLines : Boolean = false ,
click:(diary:BMobDiary)->Unit={}){
    Card(
        backgroundColor= Color.White,
        contentColor=Color.White,
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .then(
                if (hasMaxLines) {
                    Modifier.clickable {
                        click(diary)
                    }
                } else {
                    Modifier
                }
            )
         ) {
        if (diary.content.isNotEmpty()){
            Column(
                Modifier
                    .padding(6.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                LoadImage(url = diary.diaryImg,  contentDescription = "Image detail" ,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = diary.content ,
                    color = Color.DarkGray ,
                    maxLines = if (hasMaxLines) 5 else Int.MAX_VALUE,
                    overflow = TextOverflow.Ellipsis)
            }
        }
    }
}


