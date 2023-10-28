package com.example.basket.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.core.view.WindowCompat
import com.example.basket.AppBase
import com.example.basket.R
import com.example.basket.entity.SizeElement
import com.example.basket.entity.TypeText
import com.example.basket.navigation.ScreenDestination

val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_errorContainer,
    onErrorContainer = md_theme_light_onErrorContainer,
    outline = md_theme_light_outline,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    inverseSurface = md_theme_light_inverseSurface,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)
val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,  //FAB containerColor
    onTertiaryContainer = md_theme_dark_onTertiaryContainer, //FAB content color
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onErrorContainer = md_theme_dark_onErrorContainer,
    outline = md_theme_dark_outline,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,  //Navigation button, background item lazyColumn
    onSurface = md_theme_dark_onSurface, //Navigation button
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant, //Navigation button icon tint, text item lazyColumn
    inverseSurface = md_theme_dark_inverseSurface,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {

    val darkTheme: Boolean = isSystemInDarkTheme()
    val dynamicColor: Boolean = false && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorSchemeApp = when {
        dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorSchemeApp.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
    MaterialTheme(colorScheme = colorSchemeApp, content = content, shapes = shapes)
}

@Composable fun styleApp(nameStyle: TypeText): TextStyle{

    return when( nameStyle ){
        TypeText.NAME_SCREEN -> when (AppBase.scale){
            1-> typography.headlineSmall
            2 -> typography.titleMedium
            else -> typography.headlineMedium //0
        }
        TypeText.NAME_SECTION -> when (AppBase.scale){
            1-> typography.titleLarge
            2 -> typography.titleSmall
            else -> typography.headlineSmall //0
        }
        TypeText.TEXT_IN_LIST -> when (AppBase.scale){
            1-> typography.titleLarge
            2 -> typography.titleSmall
            else -> typography.headlineSmall //0
        }
        TypeText.TEXT_IN_LIST_SMALL -> when (AppBase.scale){
            1-> typography.labelLarge
            2 -> typography.labelMedium
            else -> typography.bodyMedium //0
        }
        TypeText.EDIT_TEXT -> when (AppBase.scale){
            1-> typography.titleLarge
            2 -> typography.titleSmall
            else -> typography.headlineSmall //0
        }
        TypeText.EDIT_TEXT_TITLE -> when (AppBase.scale){
            1-> typography.labelLarge
            2 -> typography.labelMedium
            else -> typography.bodyMedium //0
        }
        TypeText.TEXT_IN_LIST_SETTING -> when (AppBase.scale){
            1-> typography.titleLarge
            2 -> typography.titleSmall
            else -> typography.headlineSmall //0
        }
        TypeText.NAME_SLIDER -> when (AppBase.scale){
            1-> typography.labelLarge
            2 -> typography.labelMedium
            else -> typography.bodyMedium //0
        }
    }
}

@Composable fun sizeApp(sizeElement: SizeElement): Dp {

    return when( sizeElement ){
        SizeElement.SIZE_FAB -> when (AppBase.scale){
            1->dimensionResource(R.dimen.size_fab_medium)
            2 ->dimensionResource(R.dimen.size_fab_small)
            else -> dimensionResource(R.dimen.size_fab_large)
        }
        SizeElement.HEIGHT_BOTTOM_BAR -> when (AppBase.scale){
            1->dimensionResource(R.dimen.height_bottom_bar_medium)
            2 ->dimensionResource(R.dimen.height_bottom_bar_small)
            else -> dimensionResource(R.dimen.height_bottom_bar_large)
        }
        SizeElement.PADDING_FAB -> when (AppBase.scale){
            1->dimensionResource(R.dimen.padding_fab_medium)
            2 ->dimensionResource(R.dimen.padding_fab_small)
            else -> dimensionResource(R.dimen.padding_fab_large)
        }
        SizeElement.OFFSET_FAB -> when (AppBase.scale){
            1->dimensionResource(R.dimen.offset_fab_medium)
            2 ->dimensionResource(R.dimen.offset_fab_small)
            else -> dimensionResource(R.dimen.offset_fab_large)
        }
        SizeElement.HEIGHT_FAB_BOX -> when (AppBase.scale){
            1->dimensionResource(R.dimen.height_fab_box_medium)
            2 ->dimensionResource(R.dimen.height_fab_box_small)
            else -> dimensionResource(R.dimen.height_fab_box_large)
        }
    }
}
@Composable fun getIdImage(screen: ScreenDestination):Int{
    val dayNight = isSystemInDarkTheme()
    return if (dayNight) screen.pictureDay else screen.pictureNight
}

//#####################################################################################################



