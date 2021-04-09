package com.szhua.foryou.data


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BMobDiary(
    val content :String ="",
    val createdAt : String ="",
    var  name :String ?="" ,
    val objectId :String ="" ,
    val updatedAt :String ="",
    @field:SerializedName("dary_img") var  diaryImg :String ?=""
):Serializable
