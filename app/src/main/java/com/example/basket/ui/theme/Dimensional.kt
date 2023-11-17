package com.example.basket.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalDim = compositionLocalOf { Dimensions() }
data class Dimensions(
    val lazyItemBottomSpacer: Dp = 3.dp,
    val activityHorizontalMargin:Dp = 16.dp,
    val activityVerticalMargin:Dp = 16.dp,
    val fbButtonPlusLength:Dp = 60.dp,
    val fbButtonSize:Dp = 100.dp,
    val viewerImageButtonCorner:Int = 0,
    val viewerImageButtonCornerMinus:Int = -90,

    val viewerImageButtonLength:Dp = 20.dp,
    val viewerImageButtonMarginEnd:Dp = 20.dp,
    val viewerImageButtonMarginVer:Dp = 8.dp,

    val cornerDefault:Dp = 6.dp,

    val lazyPaddingVer:Dp = 2.dp,
    val lazyPaddingHor:Dp = 12.dp,
    val screenPaddingHor:Dp = 12.dp,
    val lazyPaddingVer1:Dp = 8.dp,
    val lazyPaddingVer2:Dp = 4.dp,
    val lazyPaddingHor1:Dp = 1.dp,
    val lazyPaddingHor2:Dp = 16.dp,
    val screenPaddingHor1:Dp = 12.dp,
    val heightFabBox:Dp = 200.dp,

    //Size icon
    val sizeFabSmall:Dp = 40.dp,
    val sizeFabMedium:Dp = 50.dp,
    val sizeFabLarge:Dp = 60.dp,
    val paddingFabSmall:Dp = 4.dp,
    val paddingFabMedium:Dp = 6.dp,
    val paddingFabLarge:Dp = 8.dp,
    val offsetFabSmall:Dp = 60.dp,
    val offsetFabMedium:Dp = 72.dp,
    val offsetFabLarge:Dp = 80.dp,
    val heightBottomBarSmall:Dp = 50.dp,
    val heightBottomBarMedium:Dp = 60.dp,
    val heightBottomBarLarge:Dp = 70.dp,
    val heightFabBoxLarge:Dp = 280.dp,
    val heightFabBoxMedium:Dp = 240.dp,
    val heightFabBoxSmall:Dp = 200.dp,
)

