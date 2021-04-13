package com.szhua.foryou.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.szhua.foryou.api.BMobService
import com.szhua.foryou.common.Pages
import com.szhua.foryou.data.BMobDiary
import com.szhua.foryou.ui.theme.mainColor
import com.szhua.foryou.ui.theme.mainColorLight
import com.szhua.foryou.ui.theme.titleTextStyle
import com.szhua.foryou.viewmodel.MainViewModel

/**
@author  SZhua
Create at  2021/4/7
Description: 首页的展示内容；
 */
@Composable
fun Main(nav : NavHostController){
    val viewModel = viewModel<MainViewModel>()
    Surface(color= mainColorLight) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "ForYou",style = titleTextStyle,color = Color.White)
                    },
                    backgroundColor = mainColor,
                    actions = {
                        IconButton(onClick = {
                            viewModel.refreshData.value = false
                            nav.navigate(Pages.DIARIES_PAGE){
                                launchSingleTop=true
                            }
                        }) {
                            Icon(Icons.Default.EventNote,"this is icon more" ,tint = Color.White )
                        }
                    }
                )
            }) {
            val scrollState = rememberScrollState()

            var  diary by rememberSaveable { mutableStateOf(BMobDiary())}
            Card(
                backgroundColor= Color.White,
                contentColor=Color.White,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)) {
                if (diary.content.isNotEmpty()){
                    Column(
                        Modifier
                            .padding(6.dp)
                            .verticalScroll(scrollState) ,horizontalAlignment = Alignment.CenterHorizontally) {
                        LoadImage(url = diary.diaryImg,  contentDescription = "IMage detail" ,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = diary.content ,color = Color.DarkGray)
                    }
                }
            }
            val refreshData by viewModel.refreshData.observeAsState()
            LaunchedEffect(true){
                //防止重复加载
                if(refreshData==true) {
                    val result = BMobService.create().findDiaries(1, 0, "-createdAt")
                    if (result.results.isNotEmpty()) {
                        diary = result.results[0]
                    }
                }
            }
        }
    }
}


