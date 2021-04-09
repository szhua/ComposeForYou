package com.szhua.foryou.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.szhua.foryou.R


/**

@author  SZhua
Create at  2021/4/7
Description:

 */
val titleFontFamily = FontFamily(
    Font(R.font.title)
)
val contentFontFamily = FontFamily(
    Font(R.font.content)
)

val typography = Typography(
    h1=TextStyle(
        fontFamily = titleFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    body1 = TextStyle(
        fontFamily = contentFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)

val titleTextStyle =TextStyle(
    fontFamily = titleFontFamily,
    color = Color.Black,
    fontWeight = FontWeight.Medium,
    fontSize = 22.sp
)

