package com.example.shopping_list.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color

val Green500 = Color(0xFF1EB980)
val DarkBlue900 = Color(0xFF26282F)
val ScaffoldColor = Color(0xCCE0E0E0)
val BottomBackgroundColor = Color(0xFFC5C5C5)
val SelectedColor = Color(0xFF9C27B0)
val BorderBottomBar = Color(0xFF6727B0)
val Transparent = Color(0xFFFFFF)


// Rally is always dark themed.
val DarkColorPalette = darkColors(
    primary = SelectedColor,
    surface = ScaffoldColor,
    onSurface = Color.Black,
    background = ScaffoldColor,
    onBackground = Color.Black
)
val LightColorPalette = darkColors(
    primary = SelectedColor,
    surface = ScaffoldColor,
    onSurface = Color.Black,
    background = ScaffoldColor,
    onBackground = Color.Black
)
// Rally is always dark themed.
val BottomColorPalette = darkColors(
    primary = SelectedColor,
    surface = ScaffoldColor,
    onSurface = Color.Black,
    background = ScaffoldColor,
    onBackground = Color.Black
)