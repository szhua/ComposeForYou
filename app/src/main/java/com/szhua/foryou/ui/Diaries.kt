package com.szhua.foryou.ui

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.orhanobut.logger.Logger
import com.szhua.foryou.api.BMobService
import com.szhua.foryou.common.Pages
import com.szhua.foryou.components.RefreshState
import com.szhua.foryou.components.SwipeToRefreshLayout
import com.szhua.foryou.ui.theme.titleTextStyle
import com.szhua.foryou.viewmodel.DiariesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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

    val diariesViewModel = viewModel<DiariesViewModel>()
    val refreshState = RefreshState()
    var  currentPage by diariesViewModel.currentPage.observeAsState() as MutableState
    val  diaries     by diariesViewModel.diaries.observeAsState()
    var  hadMoreData by diariesViewModel.hasMoreData.observeAsState() as MutableState

    val scrollState = rememberLazyListState()

    suspend fun loadData() {
        if (hadMoreData!!) {
            val result =  withContext(Dispatchers.IO){
                BMobService.create().findDiaries(10, currentPage!! * 10, "-createdAt")
            }
            refreshState.stopLoadMore()
            refreshState.stopRefresh()
            Logger.d("StopLoadMore")
            diaries?.addAll(result.results)
            if (result.results.isEmpty()) {
                hadMoreData = false
            }
        } else {
            refreshState.stopLoadMore()
        }
    }


    val scope = rememberCoroutineScope()
    val refreshFunction=fun (){
        scope.launch {
          loadData()
      }
    }
   val refreshData by rememberUpdatedState(refreshFunction)

    SwipeToRefreshLayout(
        refreshingState = refreshState,
        onRefresh = {
            diaries!!.clear()
            currentPage = 0
            loadData()
        },
        onLoadMore = {
            currentPage = currentPage!! + 1
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
            items(diaries!!.size,
                key = { diaries!![it].objectId },
                itemContent = { index ->
                Detail(diary = diaries!![index],hasMaxLines = true,click = {
                    val params =Bundle()
                    params.putSerializable("detail",it)
                    nav.navigate(createRoute(Pages.DETAIL).hashCode(),params, navOptions {
                    })
                })
            })
        }
    }

    LaunchedEffect(true){
          if (diaries!!.isEmpty()){
               refreshData()
          }
    }


}





internal fun createRoute(route: String) = "android-app://androidx.navigation.compose/$route"

