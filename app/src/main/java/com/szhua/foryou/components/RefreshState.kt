package com.szhua.foryou.components

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.runtime.Stable
import androidx.compose.ui.layout.Remeasurement
import androidx.compose.ui.layout.RemeasurementModifier

/**
@author  SZhua
Create at  2021/4/7
Description: 对上拉加载和下拉刷新的控制；
 */

@Stable
class RefreshState {

    var refreshDataState =false
    var loadDataState=false

    @OptIn(ExperimentalMaterialApi::class)
    var swipeAbleRefreshState:SwipeableState<Boolean> ? =null

    @OptIn(ExperimentalMaterialApi::class)
    var swipeAbleLoadMoreState:SwipeableState<Boolean> ? =null


    private lateinit var remeasurement: Remeasurement

    internal val remeasurementModifier = object : RemeasurementModifier {
        override fun onRemeasurementAvailable(remeasurement: Remeasurement) {
            this@RefreshState.remeasurement = remeasurement
        }
    }

    @ExperimentalMaterialApi
    suspend fun stopRefresh(){
        swipeAbleRefreshState?.animateTo(false)
        refreshDataState=false
        remeasurement.forceRemeasure()
    }

    @OptIn(ExperimentalMaterialApi::class)
    suspend fun stopLoadMore(){
        swipeAbleLoadMoreState?.animateTo(false)
        loadDataState = false
        remeasurement.forceRemeasure()
    }

}