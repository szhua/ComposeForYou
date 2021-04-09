package com.szhua.foryou.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


/**

@author  SZhua
Create at  2021/4/7
Description:
 */


private val RefreshDistance = 80.dp


private val LoadMoreDistance = 10.dp



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToRefreshLayout(
    modifier: Modifier =Modifier,
    refreshingState: RefreshState,
    onRefresh: suspend () -> Unit ={

    },
    onLoadMore:suspend ()->Unit = {},
    refreshIndicator: @Composable  () -> Unit,
    content: @Composable () -> Unit
) {



    val scope = rememberCoroutineScope()
    val refreshDistance = with(LocalDensity.current) { RefreshDistance.toPx() }
    val loadMoreDistance = with(LocalDensity.current){ LoadMoreDistance.toPx() }
    val refreshState = rememberSwipeableState(refreshingState.refreshDataState) { newValue ->
        // 到达顶部的情况下 - 》 maxRefreshHeight
        if (newValue && !refreshingState.refreshDataState) {
            refreshingState.refreshDataState = true
            scope.launch {
                onRefresh()
            }
        }
        if (!newValue) refreshingState.refreshDataState= false
        true
    }
    val loadMoreState = rememberSwipeableState(initialValue = false){newValue->
        //到达底部的情况下
        if (newValue&&!refreshingState.loadDataState){
            refreshingState.loadDataState = true
            scope.launch {
                onLoadMore()
            }
        }
        if (!newValue) refreshingState.loadDataState= false
        true
    }

    refreshingState.swipeAbleRefreshState =refreshState
    refreshingState.swipeAbleLoadMoreState=loadMoreState

    Box(
        modifier = modifier.then(
            Modifier
                .nestedScroll(refreshState.PreUpPostDownNestedScrollConnection)
                .swipeable(
                    state = refreshState,
                    anchors = mapOf(
                        -refreshDistance to false,
                        refreshDistance to true
                    ),
                    thresholds = { _, _ -> FractionalThreshold(0.5f) },
                    orientation = Orientation.Vertical
                )
                .nestedScroll(loadMoreState.PreDownPostUpNestedScrollConnection)
                .swipeable(
                    state = loadMoreState,
                    anchors = mapOf(
                        loadMoreDistance to false,
                        -loadMoreDistance to true
                    ),
                    thresholds = { _, _ -> FractionalThreshold(0.5f) },
                    orientation = Orientation.Vertical
                )
                .then(refreshingState.remeasurementModifier)
        )
    ) {
        content()
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .offset { IntOffset(0, refreshState.offset.value.roundToInt()) }
        ) {
            if (refreshState.offset.value != -refreshDistance) {
                refreshIndicator()
            }
        }

        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .offset { IntOffset(0, loadMoreState.offset.value.roundToInt()) }
        ) {
            if (loadMoreState.offset.value != loadMoreDistance) {
                refreshIndicator()
            }
        }

        /**
         * Guaranteed execution once ；
         */
        val ifRefreshDataState by rememberUpdatedState{
            if (refreshingState.refreshDataState){
                scope.launch {
                    onRefresh()
                }
            }
        }

        //一开始时候进行展示 refresh 的 indicator ；
        LaunchedEffect(refreshingState.refreshDataState) {
            refreshState.animateTo(refreshingState.refreshDataState)
            ifRefreshDataState()
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
private val <T> SwipeableState<T>.PreDownPostUpNestedScrollConnection : NestedScrollConnection
    get() = MNestedScrollConnection(this,false)

/**
 这个东西会分配事件；

 onPreScroll 滑动之前 = 》 example: 上滑的时候，（若此时 refreshIndicator在显示 ）控制 refreshIndicator 滑动，并将 refreshIndicator的位移量返回 作为已经消费的量。

 onPostScroll 滑动处理完成后，若有需要处理的滑动，那么再进行分配， 如 ： 下拉不动的情况下，这时候就有未处理的位移，剩下的交给 refreshIndicator 往下滑动即可

 同理  onPreFling  onPostFling

 ps：这里都返回 zero 就是 给 parent 说明未进行处理， 其他滑动继续进行fling ；保持其流畅性 。

 */
@OptIn(ExperimentalMaterialApi::class)
private val <T> SwipeableState<T>.PreUpPostDownNestedScrollConnection: NestedScrollConnection
    get() = MNestedScrollConnection(this,true)




@OptIn(ExperimentalMaterialApi::class)
private open class MNestedScrollConnection<T>(val state: SwipeableState<T>,val  refresh:Boolean) : NestedScrollConnection{
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.toFloat()
        return if (refresh){
            //向下滑动的情况下，state 控制 进行drag；
            if (delta < 0  && source == NestedScrollSource.Drag) {
                state.performDrag(delta = delta).toOffset()
            } else {
                Offset.Zero
            }
        }else{
            //向下滑动的时候 state 控制进行 drag
            if (delta >= 0 && source == NestedScrollSource.Drag) {
                state.performDrag(delta).toOffset()
            } else {
                Offset.Zero
            }
        }
    }


    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        return if (source == NestedScrollSource.Drag) {
            state.performDrag(available.toFloat()).toOffset()
        } else {
            Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        val toFling = Offset(available.x, available.y).toFloat()
        if (refresh && toFling<0 ){
            state.performFling(velocity = toFling)
        }
        if (!refresh && toFling>0 ){
            state.performFling(velocity = toFling)
        }
        return Velocity.Zero
    }

    override suspend fun onPostFling(
        consumed: Velocity,
        available: Velocity
    ): Velocity {
        //loadMore 的情况下 处理多余的fling ；
        if (!refresh&&available.y!=0f){
            state.performDrag(available.y)
        }
        state.performFling(velocity = Offset(available.x, available.y).toFloat())
        return Velocity.Zero
    }

    private fun Float.toOffset(): Offset = Offset(0f, this)

    private fun Offset.toFloat(): Float = this.y


}
