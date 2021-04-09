package com.szhua.foryou.data

import com.google.gson.annotations.SerializedName

data class BMobDiariesResponse(
    @field:SerializedName("results") val results :List<BMobDiary>,
)
