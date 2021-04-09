package com.szhua.foryou.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp


/**

@author  SZhua
Create at  2021/4/7
Description:

 */


val shapes = Shapes(
    // small = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(2.dp),
    medium = RoundedCornerShape(6.dp, 6.dp, 6.dp, 6.dp),
    large = RoundedCornerShape(8.dp)
)

val editShapes = Shapes(
    // small = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(18.dp, 4.dp, 4.dp, 4.dp),
    medium = RoundedCornerShape(38.dp, 4.dp, 4.dp, 4.dp),
    large = RoundedCornerShape(6.dp, 6.dp, 0.dp, 0.dp)
)
