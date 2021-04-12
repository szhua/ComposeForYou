package com.szhua.foryou.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szhua.foryou.components.RefreshState
import com.szhua.foryou.data.BMobDiary


/**

@author  SZhua
Create at  2021/4/12
Description:

 */


class DiariesViewModel :ViewModel() {

    val diaries = MutableLiveData(
        SnapshotStateList<BMobDiary>()
    )
    val hasMoreData = MutableLiveData(true)
    val currentPage = MutableLiveData(0)

    val refreshState = MutableLiveData(RefreshState())

}