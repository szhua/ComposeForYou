package com.szhua.foryou.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable



/**
@author  SZhua
Create at  2021/4/7
Description: 主题相关
 */
private val LightColorPalette = lightColors(
    primary = mainColor,
    primaryVariant = mainColor,
    secondary = mainColor,
    background = white,
    surface = whit850,
    onPrimary = gray,
    onSecondary = white,
    onBackground = mainColorLight,
    onSurface = gray
)



@Composable
fun ForYouTheme(content : @Composable ()->Unit){
    val colors  = LightColorPalette
    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
