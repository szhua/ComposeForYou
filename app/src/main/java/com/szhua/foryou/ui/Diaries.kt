package com.szhua.foryou.ui

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.*
import androidx.navigation.compose.navigate
import com.orhanobut.logger.Logger
import com.szhua.foryou.api.BMobService
import com.szhua.foryou.common.Pages
import com.szhua.foryou.components.RefreshState
import com.szhua.foryou.components.SwipeToRefreshLayout
import com.szhua.foryou.data.BMobDiary
import com.szhua.foryou.ui.theme.titleTextStyle
import kotlinx.coroutines.launch


/**
@author  SZhua
Create at  2021/4/7
Description: 日记列表；
 */


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DiariesScreen(nav :NavHostController){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Diaries", style = titleTextStyle, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        nav.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBackIos, "this is icon more", tint = Color.White)
                    }
                },
            )
        }
    ) {
        Diaries(nav = nav)
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Diaries(nav: NavHostController) {
    val refreshState by remember {
        val refreshState = RefreshState()
        refreshState.refreshDataState = true
        mutableStateOf(refreshState)
    }
    val scope = rememberCoroutineScope()

    val currentPage = remember {
        mutableStateOf(0)
    }
    val diaries = remember {
        mutableStateListOf<BMobDiary>()
    }


    var hadMoreData by remember {
        mutableStateOf(true)
    }
    val scrollState = rememberLazyListState()

    suspend fun loadData() {
        if (hadMoreData) {
            val result = BMobService.create().findDiaries(10, currentPage.value * 10, "-createdAt")
            diaries.addAll(result.results)
            refreshState.stopLoadMore()
            refreshState.stopRefresh()
            if (result.results.isEmpty()) {
                hadMoreData = false
            }
        } else {
            refreshState.stopLoadMore()
        }
    }

    SwipeToRefreshLayout(
        refreshingState = refreshState,
        onRefresh = {
            diaries.clear()
            currentPage.value = 0
            loadData()
        },
        onLoadMore = {
            currentPage.value = currentPage.value + 1
            loadData()
        },
        refreshIndicator = {
            Surface(elevation = 1.dp, shape = CircleShape) {
                CircularProgressIndicator(
                    strokeWidth = 4.dp,
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }, modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(state = scrollState) {
            items(diaries.size, key = {
                diaries[it].objectId
            }, itemContent = { index ->
                Detail(diary = diaries[index],hasMaxLines = true,click = {
                    val params =Bundle()
                    params.putSerializable("detail",it)
                    nav.navigate(createRoute(Pages.DETAIL).hashCode(),params)
                })
            })
        }
    }
}





internal fun createRoute(route: String) = "android-app://androidx.navigation.compose/$route"

