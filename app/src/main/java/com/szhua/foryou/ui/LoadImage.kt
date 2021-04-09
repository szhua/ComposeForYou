package com.szhua.foryou.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.szhua.foryou.ui.theme.compositedOnSurface
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun LoadImage(
    url: String ?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholderColor: Color? = MaterialTheme.colors.compositedOnSurface(alpha = 0.2f)
) {
    CoilImage(
        data = url?:"",
        modifier = modifier,
        contentDescription = contentDescription,
        contentScale = contentScale,
        fadeIn = true,
        loading = {
            if (placeholderColor != null) {
                Spacer(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(placeholderColor)
                )
            }
        }
    )
}