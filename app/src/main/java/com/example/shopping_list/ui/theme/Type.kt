package com.example.shopping_list.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.DeviceFontFamilyName
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.shopping_list.R

private val ScanFontFamily = FontFamily(
    Font(DeviceFontFamilyName("sans-serif-condensed"), FontWeight.Normal),
    Font(DeviceFontFamilyName("sans-serif-condensed"), FontWeight.SemiBold),
)
val RobotoCondensed = FontFamily(
//    Font(R.font.roboto_condensed_regular, FontWeight.Normal),
    Font(R.font.economica, FontWeight.Normal),
    Font(R.font.economica, FontWeight.Bold)
)
val typographyApp = Typography(
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
//val typography = Typography(

//    h1 = TextStyle(
//        fontFamily = ScanFontFamily,
//        fontWeight = FontWeight.W100,
//        fontSize = 24.sp,
//        letterSpacing = 1.5.sp
//    ),
//    h2 = TextStyle(
////        fontWeight = FontWeight.SemiBold,
//        fontSize = 20.sp,
////        letterSpacing = 1.5.sp
//    ),
//    h3 = TextStyle(
//        fontWeight = FontWeight.W400,
//        fontSize = 16.sp
//    ),
//    h4 = TextStyle(
//        fontWeight = FontWeight.W700,
//        fontSize = 12.sp
//    ),
//    h5 = TextStyle(
//        fontWeight = FontWeight.W700,
//        fontSize = 8.sp
//    ),
//    h6 = TextStyle(
//        fontWeight = FontWeight.Normal,
//        fontSize = 6.sp,
//        lineHeight = 20.sp,
//        letterSpacing = 3.sp
//    ),
//
//    body1 = TextStyle(
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        letterSpacing = 0.1.em
//    ),
//    body2 = TextStyle(
//        fontWeight = FontWeight.Normal,
//        fontSize = 14.sp,
//        lineHeight = 20.sp,
//        letterSpacing = 0.1.em
//    ),
//    button = TextStyle(
//        fontWeight = FontWeight.Bold,
//        fontSize = 14.sp,
//        lineHeight = 16.sp,
//        letterSpacing = 0.2.em
//    ),
//    caption = TextStyle(
//        fontWeight = FontWeight.W500,
//        fontSize = 12.sp
//    ),
//    overline = TextStyle(
//        fontWeight = FontWeight.W500,
//        fontSize = 10.sp
//    )
//)
